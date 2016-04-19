(function() {
    'use strict';

    angular
        .module('vehicleTrackerApp')
        .controller('StammdatenDialogController', StammdatenDialogController);

    StammdatenDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Stammdaten'];

    function StammdatenDialogController ($scope, $stateParams, $uibModalInstance, entity, Stammdaten) {
        var vm = this;
        vm.stammdaten = entity;
        vm.load = function(id) {
            Stammdaten.get({id : id}, function(result) {
                vm.stammdaten = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('vehicleTrackerApp:stammdatenUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.stammdaten.id !== null) {
                Stammdaten.update(vm.stammdaten, onSaveSuccess, onSaveError);
            } else {
                Stammdaten.save(vm.stammdaten, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
