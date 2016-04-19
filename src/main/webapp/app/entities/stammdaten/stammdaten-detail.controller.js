(function() {
    'use strict';

    angular
        .module('vehicleTrackerApp')
        .controller('StammdatenDetailController', StammdatenDetailController);

    StammdatenDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Stammdaten'];

    function StammdatenDetailController($scope, $rootScope, $stateParams, entity, Stammdaten) {
        var vm = this;
        vm.stammdaten = entity;
        vm.load = function (id) {
            Stammdaten.get({id: id}, function(result) {
                vm.stammdaten = result;
            });
        };
        var unsubscribe = $rootScope.$on('vehicleTrackerApp:stammdatenUpdate', function(event, result) {
            vm.stammdaten = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
