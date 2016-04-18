(function() {
    'use strict';

    angular
        .module('vehicleTrackerApp')
        .controller('OrderDeleteController',OrderDeleteController);

    OrderDeleteController.$inject = ['$uibModalInstance', 'entity', 'Order'];

    function OrderDeleteController($uibModalInstance, entity, Order) {
        var vm = this;
        vm.order = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Order.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
