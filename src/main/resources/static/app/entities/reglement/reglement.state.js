(function() {
    'use strict';

    angular
        .module('myApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('reglement', {
            parent: 'app',
            url: '/reglements',
            data: {
                authorities: ['ADMIN', 'USER'],
                pageTitle: 'myApp.reglement.home.title'
            },
            templateUrl: 'app/entities/reglement/reglements.html',
            controller: 'ReglementController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('reglement');
                    $translatePartialLoader.addPart('reglement');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('reglement-detail', {
            parent: 'admin',
            url: '/reglements/{reglementId}',
            params : { reglementId: null },
            data: {
                authorities: ['ADMIN'],
                pageTitle: 'myApp.reglement.detail.title'
            },
            templateUrl: 'app/entities/reglement/reglement-detail.html',
            controller: 'ReglementDetailController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('reglement');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Reglement',  function($stateParams, Reglement) {
                    var reglement = {};
                    return Reglement.get($stateParams.reglementId)
                    .then(function(response){
                        reglement = response.data;
                        //return response.data;
                    })
                    .then(function(response){
                        return Reglement.getJustificatif(reglement.id)
                          .then(function(response) {
                            reglement.justificatif = arrayBufferToBase64(response.data);
                            return reglement;
                        },
                        function(response) {
                            reglement.justificatif = null;
                            return reglement;
                        });
                    })
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'reglement',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('reglement-detail.edit', {
            parent: 'reglement-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reglement/reglement-dialog.html',
                    controller: 'ReglementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: ['Reglement',  function(Reglement) {
                             var reglement = {};
                              return Reglement.get($stateParams.reglementId)
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
        .state('reglement.new', {
            //url: '/new',
            data: {
                authorities: ['ADMIN', 'USER']
            },
            onEnter: ['$state', '$uibModal', function($state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reglement/reglement-dialog.html',
                    controller: 'ReglementDialogController',
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
                    $state.go('reglement', null, { reload: 'reglement' });
                }, function() {
                    $state.go('reglement');
                });
            }]
        })
        .state('reglement.edit', {
            parent: 'reglement',
            url: '/{reglementId}/edit',
            params : { reglementId: null },
            data: {
                authorities: ['ADMIN', 'USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reglement/reglement-dialog.html',
                    controller: 'ReglementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: ['Reglement',  function(Reglement) {
                             var reglement = {};
                             return Reglement.get($stateParams.reglementId)
                             .then(function(response){
                                reglement = response.data;
                             })
                             .then(function(response){
                                return Reglement.getJustificatif(reglement.id)
                                  .then(function(response) {
                                    reglement.justificatif = arrayBufferToBase64(response.data);
                                    return reglement;
                                },
                                function(response) {
                                    reglement.justificatif = null;
                                    return reglement;
                                });
                            })
                        }]
                    }
                }).result.then(function() {
                    $state.go('reglement', null, { reload: 'reglement' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('reglement.delete', {
            parent: 'reglement',
            url: '/{reglementId}/delete',
            params : { reglementId: null },
            data: {
                authorities: ['ADMIN', 'USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reglement/reglement-delete-dialog.html',
                    controller: 'ReglementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Reglement',  function(Reglement) {
                            console.log($stateParams.reglementId)
                             return Reglement.get($stateParams.reglementId)
                             .then(function(response){
                                return response.data;
                            })
                        }]
                    }
                }).result.then(function() {
                    $state.go('reglement', null, { reload: 'reglement' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

    function arrayBufferToBase64(buffer) {
       var binary = '';
       var bytes = new Uint8Array(buffer);
       var len = bytes.byteLength;
       for (var i = 0; i < len; i++) {
         binary += String.fromCharCode(bytes[i]);
       }
       return window.btoa(binary);
    }
})();
