(function() {
    'use strict';

    angular
        .module('vehicleTrackerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stammdaten', {
            parent: 'entity',
            url: '/stammdaten?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vehicleTrackerApp.stammdaten.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stammdaten/stammdatens.html',
                    controller: 'StammdatenController',
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
                    $translatePartialLoader.addPart('stammdaten');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('stammdaten-detail', {
            parent: 'entity',
            url: '/stammdaten/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vehicleTrackerApp.stammdaten.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stammdaten/stammdaten-detail.html',
                    controller: 'StammdatenDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stammdaten');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Stammdaten', function($stateParams, Stammdaten) {
                    return Stammdaten.get({id : $stateParams.id});
                }]
            }
        })
        .state('stammdaten.new', {
            parent: 'stammdaten',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stammdaten/stammdaten-dialog.html',
                    controller: 'StammdatenDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                deviceId: null,
                                driver: null,
                                vehicle: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('stammdaten', null, { reload: true });
                }, function() {
                    $state.go('stammdaten');
                });
            }]
        })
        .state('stammdaten.edit', {
            parent: 'stammdaten',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stammdaten/stammdaten-dialog.html',
                    controller: 'StammdatenDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Stammdaten', function(Stammdaten) {
                            return Stammdaten.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('stammdaten', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stammdaten.delete', {
            parent: 'stammdaten',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stammdaten/stammdaten-delete-dialog.html',
                    controller: 'StammdatenDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Stammdaten', function(Stammdaten) {
                            return Stammdaten.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('stammdaten', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
