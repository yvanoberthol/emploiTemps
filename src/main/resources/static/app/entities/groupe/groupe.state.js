(function() {
    'use strict';

    angular
        .module('myApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('groupe', {
            parent: 'admin',
            url: '/groupes',
            data: {
                authorities: ['ADMIN', 'SUPER'],
                pageTitle: 'myApp.groupe.home.title'
            },
            templateUrl: 'app/entities/groupe/groupes.html',
            controller: 'GroupeController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('groupe');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        /*
        .state('groupe-detail', {
            parent: 'app',
            url: '/groupe/{groupeId}',
            params : { groupeId: null },
            data: {
                authorities: ['ADMIN', 'SUPER'],
                pageTitle: 'myApp.groupe.detail.title'
            },
            templateUrl: 'app/entities/groupe/groupe-detail.html',
            controller: 'GroupeDetailController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('groupe');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Groupe',  function($stateParams, Groupe) {
                     var groupe = {};
                     return Groupe.get($stateParams.groupeId)
                     .then(function(response){
                        groupe = response.data;
                        return response.data;
                    })
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'groupe',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })

        .state('groupe-detail.edit', {
            parent: 'groupe-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ADMIN', 'SUPER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/groupe/groupe-dialog.html',
                    controller: 'GroupeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: ['Groupe',  function(Groupe) {
                             var groupe = {};
                              return Groupe.get($stateParams.groupeId)
                              .then(function(response){
                                 groupe = response.data;
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
        */
        .state('groupe.new', {
            //url: '/new',
            data: {
                authorities: ['ADMIN', 'SUPER']
            },
            onEnter: ['$state', '$uibModal', function($state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/groupe/groupe-dialog.html',
                    controller: 'GroupeDialogController',
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
                    $state.go('groupe', null, { reload: 'groupe' });
                }, function() {
                    $state.go('groupe');
                });
            }]
        })

        .state('groupe.edit', {
            parent: 'groupe',
            url: '/{groupeId}/edit',
            params : { groupeId: null },
            data: {
                authorities: ['ADMIN', 'SUPER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/groupe/groupe-dialog.html',
                    controller: 'GroupeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: ['Groupe',  function(Groupe) {
                             var groupe = {};
                             return Groupe.get($stateParams.groupeId)
                             .then(function(response){
                                return response.data;
                            })
                        }]
                    }
                }).result.then(function() {
                    $state.go('groupe', null, { reload: 'groupe' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('groupe.delete', {
            parent: 'groupe',
            url: '/{groupeId}/delete',
            params : { groupeId: null },
            data: {
                authorities: ['ADMIN', 'SUPER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/groupe/groupe-delete-dialog.html',
                    controller: 'GroupeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Groupe',  function(Groupe) {
                            console.log($stateParams.groupeId)
                             return Groupe.get($stateParams.groupeId)
                             .then(function(response){
                                return response.data;
                            })
                        }]
                    }
                }).result.then(function() {
                    $state.go('groupe', null, { reload: 'groupe' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
