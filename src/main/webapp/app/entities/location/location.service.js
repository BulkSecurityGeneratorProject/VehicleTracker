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
                    data = angular.fromJson(data);
                    data.time = DateUtils.convertDateTimeFromServer(data.time);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
