(function() {
    'use strict';

    angular
        .module('vehicleTrackerApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', 'Stammdaten', 'AlertService'];

    function HomeController ($scope, Principal, LoginService, Stammdaten, AlertService) {
        var vm = this;
        vm.map = null;
        vm.account = null;
        vm.isAuthenticated = null;
        vm.markers = null;
        vm.login = LoginService.open;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }

        vm.map = null;
        vm.options = null;

        initMap();
        function initMap() {
            vm.map = {center: {latitude: 51.219053, longitude: 4.404418 }, markers: [], zoom: 14 };
            vm.options = {scrollwheel: false};
            vm.markers = vm.map.markers;
        }

        function setMarkerAndCenterAround(id, latitude, longitude) {
            var marker;

            marker = {
                id: id,
                coords: {
                    latitude: latitude,
                    longitude: longitude
                }
            };
            vm.map.markers.push(marker); // add marker to array
            vm.map.center.latitude = latitude;
            vm.map.center.longitude = longitude;
        }

        setMarkerAndCenterAround(1, 51.219053, 4.404418);
        //setMarker(vm.map, new google.maps.LatLng(51.508515, -0.125487), 'London', 'Just some content');


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

    }
})();
