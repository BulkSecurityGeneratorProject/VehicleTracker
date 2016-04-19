(function() {
    'use strict';

    angular
        .module('vehicleTrackerApp')
        .controller('OrderDialogController', OrderDialogController);

    OrderDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Order'];

    function OrderDialogController ($scope, $stateParams, $uibModalInstance, entity, Order) {
        var vm = this;
        vm.order = entity;
        vm.load = function(id) {
            Order.get({id : id}, function(result) {
                vm.order = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('vehicleTrackerApp:orderUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.order.id !== null) {
                Order.update(vm.order, onSaveSuccess, onSaveError);
            } else {
                Order.save(vm.order, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.deadline = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
