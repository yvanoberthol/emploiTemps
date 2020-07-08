(function() {
    'use strict';

    angular
        .module('myApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('annee', {
            parent: 'admin',
            url: '/annees',
            data: {
                authorities: ['ADMIN'],
                pageTitle: 'myApp.annee.home.title'
            },
            templateUrl: 'app/admin/annee/annees.html',
            controller: 'AnneeController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('annee');
                    $translatePartialLoader.addPart('classe');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('annee-detail', {
            parent: 'admin',
            url: '/annees/{anneeId}',
            params : { anneeId: null },
            data: {
                authorities: ['ADMIN'],
                pageTitle: 'myApp.annee.detail.title'
            },
            templateUrl: 'app/admin/annee/annee-detail.html',
            controller: 'AnneeDetailController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('annee');
                    $translatePartialLoader.addPart('promo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Annee',  function($stateParams, Annee) {
                     var annee = {};
                     return Annee.get($stateParams.anneeId)
                     .then(function(response){
                        annee = response.data;
                        return response.data;
                    })
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'annee',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('annee-detail.matieres', {
            parent: 'annee-detail',
            url: '/promo/{promoId}/matieres',
            params : { promoId: null },
            data: {
                authorities: ['ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/admin/annee/annee-detail-matieres-dialog.html',
                    controller: 'AnneeDetailMatieresDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'xl',
                    resolve: {
                        entity: ['Promo',  function(Promo) {
                              return Promo.get($stateParams.promoId)
                              .then(function(response){
                                 return response.data;
                             })
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('annee-detail.edit-scolarite', {
            parent: 'annee-detail',
            url: '/promo/{promoId}/edit-mt-scolarite',
            params : { promoId: null },
            data: {
                authorities: ['ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/admin/annee/annee-detail-edit-scolarite-dialog.html',
                    controller: 'AnneeDetailEditScolariteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'sm',
                    resolve: {
                        entity: ['Promo',  function(Promo) {
                              return Promo.get($stateParams.promoId)
                              .then(function(response){
                                 console.log(response.data)
                                 return response.data;
                             })
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('annee.new', {
            url: '/new',
            data: {
                authorities: ['ADMIN']
            },
            onEnter: ['$state', '$uibModal', function($state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/admin/annee/annee-dialog.html',
                    controller: 'AnneeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'false',
                    size: 'md',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('annee', null, { reload: 'annee' });
                }, function() {
                    $state.go('annee');
                });
            }]
        })
        .state('annee.edit', {
            parent: 'annee',
            url: '/{anneeId}/edit',
            params : { anneeId: null },
            data: {
                authorities: ['ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/admin/annee/annee-dialog.html',
                    controller: 'AnneeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: ['Annee',  function(Annee) {
                             var annee = {};
                             return Annee.get($stateParams.anneeId)
                             .then(function(response){
                                return response.data;
                            })
                        }]
                    }
                }).result.then(function() {
                    $state.go('annee', null, { reload: 'annee' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('annee.set-active', {
            parent: 'annee',
            url: '/{anneeId}/set-active',
            params : { anneeId: null },
            data: {
                authorities: ['ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/admin/annee/annee-set-active-dialog.html',
                    controller: 'AnneeSetActiveController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Annee',  function(Annee) {
                            console.log($stateParams.anneeId)
                             return Annee.get($stateParams.anneeId)
                             .then(function(response){
                                return response.data;
                            })
                        }]
                    }
                }).result.then(function() {
                    $state.go('annee', null, { reload: 'annee' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('annee.delete', {
            parent: 'annee',
            url: '/{anneeId}/delete',
            params : { anneeId: null },
            data: {
                authorities: ['ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/admin/annee/annee-delete-dialog.html',
                    controller: 'AnneeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Annee',  function(Annee) {
                            console.log($stateParams.anneeId)
                             return Annee.get($stateParams.anneeId)
                             .then(function(response){
                                return response.data;
                            })
                        }]
                    }
                }).result.then(function() {
                    $state.go('annee', null, { reload: 'annee' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
