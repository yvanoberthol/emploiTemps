(function() {
    'use strict';

    angular
        .module('myApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('trimestre', {
            parent: 'app',
            url: '/trimestres',
            data: {
                authorities: ['ADMIN', 'SUPER', 'USER'],
                pageTitle: 'myApp.trimestre.home.title'
            },
            templateUrl: 'app/entities/trimestre/trimestres.html',
            controller: 'TrimestreController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('trimestre');
                    $translatePartialLoader.addPart('product');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('trimestre-detail', {
            parent: 'admin',
            url: '/trimestre/{trimestreId}',
            params : { trimestreId: null },
            data: {
                authorities: ['ADMIN', 'SUPER', 'USER'],
                pageTitle: 'myApp.trimestre.detail.title'
            },
            templateUrl: 'app/entities/trimestre/trimestre-detail.html',
            controller: 'TrimestreDetailController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('trimestre');
                    $translatePartialLoader.addPart('product');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Trimestre',  function($stateParams, Trimestre) {
                     var trimestre = {};
                     return Trimestre.get($stateParams.trimestreId)
                     .then(function(response){
                        trimestre = response.data;
                        return response.data;
                    })
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'trimestre',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('trimestre-detail.edit', {
            parent: 'trimestre-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ADMIN', 'SUPER', 'USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trimestre/trimestre-dialog.html',
                    controller: 'TrimestreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: ['Trimestre',  function(Trimestre) {
                             var trimestre = {};
                              return Trimestre.get($stateParams.trimestreId)
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
        .state('trimestre.new', {
            //url: '/new',
            data: {
                authorities: ['ADMIN', 'SUPER', 'USER']
            },
            onEnter: ['$state', '$uibModal', 'session', function($state, $uibModal, session) {
                $uibModal.open({
                    templateUrl: 'app/entities/trimestre/trimestre-dialog.html',
                    controller: 'TrimestreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'false',
                    size: 'md',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null,
                                anneeId: session.getAnneeId()
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('trimestre', null, { reload: 'trimestre' });
                }, function() {
                    $state.go('trimestre');
                });
            }]
        })
        .state('trimestre.edit', {
            parent: 'trimestre',
            url: '/{trimestreId}/edit',
            params : { trimestreId: null },
            data: {
                authorities: ['ADMIN', 'SUPER', 'USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trimestre/trimestre-dialog.html',
                    controller: 'TrimestreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: ['Trimestre',  function(Trimestre) {
                             var trimestre = {};
                             return Trimestre.get($stateParams.trimestreId)
                             .then(function(response){
                                return response.data;
                            })
                        }]
                    }
                }).result.then(function() {
                    $state.go('trimestre', null, { reload: 'trimestre' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('trimestre.new-sequence', {
            url: '/{trimestreId}/new-sequence',
            data: {
                authorities: ['ADMIN', 'SUPER', 'USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sequence/sequence-dialog.html',
                    controller: 'SequenceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'false',
                    size: 'md',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null,
                                trimestreId: $stateParams.trimestreId
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('trimestre', null, { reload: 'trimestre' });
                }, function() {
                    $state.go('trimestre');
                });
            }]
        })
        .state('trimestre.edit-sequence', {
            parent: 'trimestre',
            url: '/{sequenceId}/edit-sequence',
            params : { sequenceId: null },
            data: {
                authorities: ['ADMIN', 'SUPER', 'USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sequence/sequence-dialog.html',
                    controller: 'SequenceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: ['Sequence',  function(Sequence) {
                             var sequence = {};
                             return Sequence.get($stateParams.sequenceId)
                             .then(function(response){
                                return response.data;
                            })
                        }]
                    }
                }).result.then(function() {
                    $state.go('trimestre', null, { reload: 'trimestre' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('trimestre.delete-sequence', {
            parent: 'trimestre',
            url: '/{sequenceId}/delete-sequence',
            params : { sequenceId: null },
            data: {
                authorities: ['ADMIN', 'SUPER', 'USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sequence/sequence-delete-dialog.html',
                    controller: 'SequenceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Sequence',  function(Sequence) {
                             return Sequence.get($stateParams.sequenceId)
                             .then(function(response){
                                return response.data;
                            })
                        }]
                    }
                }).result.then(function() {
                    $state.go('trimestre', null, { reload: 'trimestre' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('trimestre.delete', {
            parent: 'trimestre',
            url: '/{trimestreId}/delete',
            params : { trimestreId: null },
            data: {
                authorities: ['ADMIN', 'SUPER', 'USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trimestre/trimestre-delete-dialog.html',
                    controller: 'TrimestreDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Trimestre',  function(Trimestre) {
                             return Trimestre.get($stateParams.trimestreId)
                             .then(function(response){
                                return response.data;
                            })
                        }]
                    }
                }).result.then(function() {
                    $state.go('trimestre', null, { reload: 'trimestre' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
