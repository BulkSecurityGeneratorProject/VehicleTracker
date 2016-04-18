(function() {
    'use strict';

    angular
        .module('vehicleTrackerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('order', {
            parent: 'entity',
            url: '/order?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vehicleTrackerApp.order.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/order/orders.html',
                    controller: 'OrderController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('order');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('order-detail', {
            parent: 'entity',
            url: '/order/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vehicleTrackerApp.order.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/order/order-detail.html',
                    controller: 'OrderDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('order');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Order', function($stateParams, Order) {
                    return Order.get({id : $stateParams.id});
                }]
            }
        })
        .state('order.new', {
            parent: 'order',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order/order-dialog.html',
                    controller: 'OrderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                deviceId: null,
                                orderNr: null,
                                fromLatitude: null,
                                fromLongitude: null,
                                toLongitude: null,
                                toLatitude: null,
                                deadline: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('order', null, { reload: true });
                }, function() {
                    $state.go('order');
                });
            }]
        })
        .state('order.edit', {
            parent: 'order',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order/order-dialog.html',
                    controller: 'OrderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Order', function(Order) {
                            return Order.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('order', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('order.delete', {
            parent: 'order',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order/order-delete-dialog.html',
                    controller: 'OrderDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Order', function(Order) {
                            return Order.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('order', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
