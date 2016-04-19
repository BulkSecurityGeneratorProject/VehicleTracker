(function() {
    'use strict';

    angular
        .module('vehicleTrackerApp')
        .controller('LocationDialogController', LocationDialogController);

    LocationDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Location'];

    function LocationDialogController ($scope, $stateParams, $uibModalInstance, entity, Location) {
        var vm = this;
        vm.location = entity;
        vm.load = function(id) {
            Location.get({id : id}, function(result) {
                vm.location = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('vehicleTrackerApp:locationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.location.id !== null) {
                Location.update(vm.location, onSaveSuccess, onSaveError);
            } else {
                Location.save(vm.location, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.time = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
