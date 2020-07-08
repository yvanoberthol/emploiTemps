(function() {
    'use strict';

    angular
        .module('myApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('inscription', {
            parent: 'app',
            url: '/inscriptions',
            data: {
                authorities: ['ADMIN', 'USER'],
                pageTitle: 'myApp.inscription.home.title'
            },
            templateUrl: 'app/entities/inscription/inscriptions.html',
            controller: 'InscriptionController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('inscription');
                    $translatePartialLoader.addPart('product');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('parent', {
            parent: 'app',
            url: '/parents',
            params : { promoId: null },
            data: {
                authorities: ['ADMIN', 'USER', 'SUPER']
            },
            templateUrl: 'app/entities/inscription/parents.html',
            controller: 'InscriptionController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('inscription');
                    $translatePartialLoader.addPart('student');
                    $translatePartialLoader.addPart('sms');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('inscription-detail', {
            parent: 'admin',
            url: '/inscription/{inscriptionId}',
            params : { inscriptionId: null },
            data: {
                authorities: ['ADMIN'],
                pageTitle: 'myApp.inscription.detail.title'
            },
            templateUrl: 'app/entities/inscription/inscription-detail.html',
            controller: 'InscriptionDetailController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('inscription');
                    $translatePartialLoader.addPart('product');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Inscription',  function($stateParams, Inscription) {
                     var inscription = {};
                     return Inscription.get($stateParams.inscriptionId)
                     .then(function(response){
                        inscription = response.data;
                        return response.data;
                    })
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'inscription',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('inscription-detail.edit', {
            parent: 'inscription-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inscription/inscription-dialog.html',
                    controller: 'InscriptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: ['Inscription',  function(Inscription) {
                             var inscription = {};
                              return Inscription.get($stateParams.inscriptionId)
                              .then(function(response){
                                 return response.data;
                             })
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('inscription.new', {
            //url: '/new',
            data: {
                authorities: ['ADMIN', 'USER']
            },
            onEnter: ['$state', '$uibModal', function($state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inscription/inscription-dialog.html',
                    controller: 'InscriptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'false',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('inscription', null, { reload: 'inscription' });
                }, function() {
                    $state.go('inscription');
                });
            }]
        })
        .state('inscription.edit', {
            parent: 'inscription',
            url: '/{inscriptionId}/edit',
            params : { inscriptionId: null },
            data: {
                authorities: ['ADMIN', 'USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inscription/inscription-dialog.html',
                    controller: 'InscriptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: ['Inscription',  function(Inscription) {
                                console.log("in state")
                             return Inscription.get($stateParams.inscriptionId)
                             .then(function(response){
                                console.log(response)
                                return response.data;
                            })
                        }]
                    }
                }).result.then(function() {
                    $state.go('inscription', null, { reload: 'inscription' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('inscription.delete', {
            parent: 'inscription',
            url: '/{studentId}/{promoId}/delete',
            params : { studentId: null, promoId: null },
            data: {
                authorities: ['ADMIN', 'USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inscription/inscription-delete-dialog.html',
                    controller: 'InscriptionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Inscription',  function(Inscription) {
                            console.log($stateParams)
                             return Inscription.get($stateParams.studentId, $stateParams.promoId)
                             .then(function(response){
                                return response.data;
                            })
                        }]
                    }
                }).result.then(function() {
                    $state.go('inscription', null, { reload: 'inscription' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
