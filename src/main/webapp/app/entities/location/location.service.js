(function() {
    'use strict';
    angular
        .module('vehicleTrackerApp')
        .factory('Location', Location);

    Location.$inject = ['$resource', 'DateUtils'];

    function Location ($resource, DateUtils) {
        var resourceUrl =  'api/locations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    try {
                        data = angular.fromJson(data);
                        data.time = DateUtils.convertDateTimeFromServer(data.time);
                    } catch (e) {
                        console.error(e);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
            'byDeviceId' : {
                method: 'GET',
                url: 'api/locations/device/:deviceId',
                transformResponse: function (data) {
                    try {
                        data = angular.fromJson(data);
                        data.time = DateUtils.convertDateTimeFromServer(data.time);
                    } catch (e) {
                        console.error(e);
                    }
                    return data;
                }
            }
        });
    }
})();
