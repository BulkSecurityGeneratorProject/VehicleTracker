(function() {
    'use strict';
    angular
        .module('vehicleTrackerApp')
        .factory('Order', Order);

    Order.$inject = ['$resource', 'DateUtils'];

    function Order ($resource, DateUtils) {
        var resourceUrl =  'api/orders/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    try {
                    data = angular.fromJson(data);
                    data.deadline = DateUtils.convertDateTimeFromServer(data.deadline);
                    } catch (e) {
                        console.error(e);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
            'byDeviceId' : {
                method: 'GET',
                url: 'api/orders/device/:deviceId',
                transformResponse: function (data) {
                    try {
                        data = angular.fromJson(data)
                        data.deadline = DateUtils.convertDateTimeFromServer(data.deadline);
                    } catch (e) {
                        console.error(e);
                    }
                    return data;
                }
            }
        });
    }
})();
