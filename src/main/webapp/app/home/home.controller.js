(function() {
    'use strict';

    angular
        .module('vehicleTrackerApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', 'Stammdaten', 'AlertService', 'uiGmapIsReady', 'Location', 'Order'];

    function HomeController ($scope, Principal, LoginService, Stammdaten, AlertService, uiGmapIsReady, Location, Order) {
        var vm = this;

        /** Map settings **/
        vm.map = null;
        vm.options = null;

        vm.directionsDisplay = new google.maps.DirectionsRenderer({draggable: true, markerOptions: {visible: false}});

        initMap();
        function initMap() {
            vm.map = {center: {latitude: 51.219053, longitude: 4.404418}, markers: [], zoom: 14, control: {}};
            vm.options = {scrollwheel: false};
        }

        function setMarkerAndCenterAround(id, title, latitude, longitude) {
            var marker;

            marker = {
                id: id,
                coords: {
                    latitude: latitude,
                    longitude: longitude,
                },
                title: title
            };

            vm.map.markers.pop();
            vm.map.markers.push(marker); // add marker to array
            vm.map.center.latitude = latitude;
            vm.map.center.longitude = longitude;
        }

        function calcRoute(latitudeFrom, longitudeFrom, latitudeTo, longitudeTo) {
            var directionsService = new google.maps.DirectionsService();
            var request = {
                origin: {lat: latitudeFrom, lng: longitudeFrom},
                destination: {lat: latitudeTo, lng: longitudeTo},
                travelMode: google.maps.TravelMode["DRIVING"]
            };
            directionsService.route(request, function (response, status) {
                if (status == google.maps.DirectionsStatus.OK) {
                    vm.directionsDisplay.setDirections(response);
                }
            });
        }

        //-----STAMMDATEN -----//

        function loadStammdaten() {
            Stammdaten.query({}, onSuccess, onError);
            function onSuccess(data) {
                vm.stammdaten = data;

            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        loadStammdaten();

        var activeId;
        $scope.showOnMap = function(deviceId, fromTimeout) {
            if (fromTimeout && deviceId != activeId) return;
            activeId = deviceId;

            Location.byDeviceId({'deviceId':deviceId}, onLocationSuccess, onError);
            Order.byDeviceId({'deviceId':deviceId}, onOrderSuccess, onError);
            function onLocationSuccess(data) {
                uiGmapIsReady.promise().then(function (maps) {
                    setMarkerAndCenterAround(deviceId, deviceId, data.latitude, data.longitude);
                });
                setTimeout(function(){$scope.showOnMap(deviceId, true)}, 30000);
            }
            function onOrderSuccess(data) {
                uiGmapIsReady.promise().then(function (maps) {
                    vm.directionsDisplay.setMap(maps[0].map);
                    calcRoute(data.fromLatitude, data.fromLongitude, data.toLatitude, data.toLongitude);
                });
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        };


    }
})();
