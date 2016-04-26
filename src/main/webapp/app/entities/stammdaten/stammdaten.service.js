(function() {
    'use strict';
    angular
        .module('vehicleTrackerApp')
        .factory('Stammdaten', Stammdaten);

    Stammdaten.$inject = ['$resource'];

    function Stammdaten ($resource) {
        var resourceUrl =  'api/stammdatens/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    try {
                        data = angular.fromJson(data);
                    } catch (e) {
                        console.error(e);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
