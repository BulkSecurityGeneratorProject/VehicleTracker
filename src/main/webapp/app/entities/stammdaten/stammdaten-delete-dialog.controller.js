(function() {
    'use strict';

    angular
        .module('vehicleTrackerApp')
        .controller('StammdatenDeleteController',StammdatenDeleteController);

    StammdatenDeleteController.$inject = ['$uibModalInstance', 'entity', 'Stammdaten'];

    function StammdatenDeleteController($uibModalInstance, entity, Stammdaten) {
        var vm = this;
        vm.stammdaten = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Stammdaten.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
