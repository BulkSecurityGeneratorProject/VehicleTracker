(function() {
    'use strict';

    angular
        .module('vehicleTrackerApp')
        .controller('OrderDetailController', OrderDetailController);

    OrderDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Order'];

    function OrderDetailController($scope, $rootScope, $stateParams, entity, Order) {
        var vm = this;
        vm.order = entity;
        vm.load = function (id) {
            Order.get({id: id}, function(result) {
                vm.order = result;
            });
        };
        var unsubscribe = $rootScope.$on('vehicleTrackerApp:orderUpdate', function(event, result) {
            vm.order = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
