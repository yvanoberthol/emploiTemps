(function() {
    'use strict';

    angular
        .module('myApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('promo', {
            parent: 'app',
            url: '/classes',
            data: {
                pageTitle: 'myApp.promo.home.title'
            },
            templateUrl: 'app/entities/promo/promos.html',
            controller: 'PromoController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('classe');
                    $translatePartialLoader.addPart('student');
                    $translatePartialLoader.addPart('promo');
                    $translatePartialLoader.addPart('matiere');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('promo-detail', {
            parent: 'app',
            abstract: true,
            url: '/classes/{promoId}',
            params : { promoId: null },
            data: {
                //authorities: ['ADMIN'],
                pageTitle: 'myApp.promo.detail.title'
            },
            templateUrl: 'app/entities/promo/promo-detail.html',
            controller: 'PromoDetailController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('promo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Promo',  function($stateParams, Promo) {
                     var promo = {};
                     return Promo.get($stateParams.promoId)
                     .then(function(response){
                        promo = response.data;
                        return response.data;
                    })
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'promo',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })

        .state('promo-detail.matieres', {
            parent: 'promo-detail',
            url: '/matieres',
            params : { promoId: null },
            data: {
                pageTitle: 'myApp.promo.detail.title'
            },
            templateUrl: 'app/entities/promo/promo-matieres.html',
            controller: 'PromoMatieresController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('classe');
                    $translatePartialLoader.addPart('matiere');
                    $translatePartialLoader.addPart('note');
                    $translatePartialLoader.addPart('groupe');
                    $translatePartialLoader.addPart('teacher');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Promo',  function($stateParams, Promo) {
                     var promo = {};
                     return Promo.get($stateParams.promoId)
                     .then(function(response){
                        promo = response.data;
                        console.log(promo)
                        return response.data;
                    })
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'promo',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('promo-detail.matieres.new', {
            //url: '/new',
            data: {
            },
            onEnter: ['$state', '$uibModal', function($state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/matiere/matiere-dialog.html',
                    controller: 'MatiereDialogController',
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
                    $state.go('promo-detail.matieres', null, { reload: 'promo-detail.matieres' });
                }, function() {
                    $state.go('promo-detail.matieres');
                });
            }]
        })
        .state('promo-detail.matieres.edit', {
            parent: 'promo-detail.matieres',
            url: '/{matiereId}/edit',
            data: {
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/matiere/matiere-dialog.html',
                    controller: 'MatiereDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: ['Matiere',  function(Matiere) {
                             var matiere = {};
                              return Matiere.get($stateParams.matiereId)
                              .then(function(response){
                                 return response.data;
                             })
                        }]
                    }
                }).result.then(function() {
                    $state.go('promo-detail.matieres', {}, { reload: 'promo-detail.matieres' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('promo-detail.matieres.delete', {
            parent: 'promo-detail.matieres',
            url: '/{matiereId}/delete',
            params : { matiereId: null },
            data: {
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/matiere/matiere-delete-dialog.html',
                    controller: 'MatiereDeleteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: ['Matiere',  function(Matiere) {
                             var matiere = {};
                              return Matiere.get($stateParams.matiereId)
                              .then(function(response){
                                console.log(response.data)
                                 return response.data;
                             })
                        }]
                    }
                }).result.then(function() {
                    $state.go('promo-detail.matieres', {}, { reload: 'promo-detail.matieres' });
                }, function() {
                    $state.go('^');
                });
            }]
        })

        .state('promo-detail.groupes', {
            parent: 'promo-detail',
            url: '/groupes',
            params : { promoId: null },
            data: {
                pageTitle: 'myApp.promo.detail.title'
            },
            templateUrl: 'app/entities/promo/promo-groupes.html',
            controller: 'PromoGroupesController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('classe');
                    $translatePartialLoader.addPart('groupe');
                    $translatePartialLoader.addPart('note');
                    $translatePartialLoader.addPart('teacher');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Promo',  function($stateParams, Promo) {
                     var promo = {};
                     return Promo.get($stateParams.promoId)
                     .then(function(response){
                        promo = response.data;
                        console.log(promo)
                        return response.data;
                    })
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'promo',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('promo-detail.groupes.new', {
            //url: '/new',
            data: {
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
                    $state.go('promo-detail.groupes', null, { reload: 'promo-detail.groupes' });
                }, function() {
                    $state.go('promo-detail.groupes');
                });
            }]
        })
        .state('promo-detail.groupes.edit', {
            parent: 'promo-detail.groupes',
            url: '/{groupeId}/edit',
            data: {
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
                    $state.go('promo-detail.groupes', {}, { reload: 'promo-detail.groupes' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('promo-detail.groupes.delete', {
            parent: 'promo-detail.groupes',
            url: '/{groupeId}/delete-groupe',
            params : { groupeId: null },
            data: {
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/groupe/groupe-delete-dialog.html',
                    controller: 'GroupeDeleteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: ['Groupe',  function(Groupe) {
                             var groupe = {};
                              return Groupe.get($stateParams.groupeId)
                              .then(function(response){
                                console.log(response.data)
                                 return response.data;
                             })
                        }]
                    }
                }).result.then(function() {
                    $state.go('promo-detail.groupes', {}, { reload: 'promo-detail.groupes' });
                }, function() {
                    $state.go('^');
                });
            }]
        })

        .state('promo.new', {
            //url: '/new',
            data: {
                authorities: ['ADMIN', 'USER', 'SUPER']
            },
            onEnter: ['$state', '$uibModal', 'session', function($state, $uibModal, session) {
                $uibModal.open({
                    templateUrl: 'app/entities/promo/promo-dialog.html',
                    controller: 'PromoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'false',
                    size: 'md',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                anneeId: session.getAnneeId(),
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('promo', null, { reload: 'promo' });
                }, function() {
                    $state.go('promo');
                });
            }]
        })
        .state('promo.edit', {
            parent: 'promo',
            url: '/{promoId}/edit',
            params : { promoId: null },
            data: {
                authorities: ['ADMIN', 'USER', 'SUPER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/promo/promo-dialog.html',
                    controller: 'PromoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: ['Promo',  function(Promo) {
                             var promo = {};
                             return Promo.get($stateParams.promoId)
                             .then(function(response){
                                return response.data;
                            })
                        }]
                    }
                }).result.then(function() {
                    $state.go('promo', null, { reload: 'promo' });
                }, function() {
                    $state.go('^');
                });
            }]
        })

        .state('promo-detail.students', {
                    parent: 'promo-detail',
                    url: '/students',
                    params : { promoId: null },
                    data: {
                        pageTitle: 'myApp.promo.detail.title'
                    },
                    templateUrl: 'app/entities/promo/promo-students.html',
                    controller: 'PromoStudentsController',
                    controllerAs: 'vm',
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('classe');
                            $translatePartialLoader.addPart('student');
                            $translatePartialLoader.addPart('note');
                            return $translate.refresh();
                        }],
                        entity: ['$stateParams', 'Promo',  function($stateParams, Promo) {
                             var promo = {};
                             return Promo.get($stateParams.promoId)
                             .then(function(response){
                                promo = response.data;
                                return response.data;
                            })
                        }],
                        previousState: ["$state", function ($state) {
                            var currentStateData = {
                                name: $state.current.name || 'promo',
                                params: $state.params,
                                url: $state.href($state.current.name, $state.params)
                            };
                            return currentStateData;
                        }]
                    }
                })
        .state('promo-detail.inscriptions', {
            parent: 'promo-detail',
            url: '/inscriptions',
            params : { promoId: null },
            data: {
                pageTitle: 'myApp.promo.detail.title'
            },
            templateUrl: 'app/entities/promo/promo-inscriptions.html',
            controller: 'PromoInscriptionsController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('classe');
                    $translatePartialLoader.addPart('student');
                    $translatePartialLoader.addPart('note');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Promo',  function($stateParams, Promo) {
                     var promo = {};
                     return Promo.get($stateParams.promoId)
                     .then(function(response){
                        promo = response.data;
                        return response.data;
                    })
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'promo',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('promo-detail.notes', {
            parent: 'promo-detail',
            url: '/edit-notes',
            params : { promoId: null },
            data: {
                //authorities: ['ADMIN', 'USER', 'SUPER'],
                pageTitle: 'myApp.promo.notes.title'
            },
            templateUrl: 'app/entities/promo/promo-notes.html',
            controller: 'PromoNotesController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('classe');
                    $translatePartialLoader.addPart('inscription');
                    $translatePartialLoader.addPart('student');
                    $translatePartialLoader.addPart('note');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Promo',  function($stateParams, Promo) {
                     var promo = {};
                     return Promo.get($stateParams.promoId)
                     .then(function(response){
                        promo = response.data;
                        return response.data;
                    })
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'promo',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('promo-detail.notes-summary', {
            parent: 'promo-detail',
            url: '/notes',
            params : { promoId: null },
            data: {
                //authorities: ['ADMIN', 'SUPER'],
                pageTitle: 'myApp.promo.notes.title'
            },
            templateUrl: 'app/entities/promo/promo-notes-summary.html',
            controller: 'PromoNotesSummaryController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('classe');
                    $translatePartialLoader.addPart('inscription');
                    $translatePartialLoader.addPart('student');
                    $translatePartialLoader.addPart('note');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Promo',  function($stateParams, Promo) {
                     var promo = {};
                     return Promo.get($stateParams.promoId)
                     .then(function(response){
                        promo = response.data;
                        return response.data;
                    })
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'promo',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('promo.delete', {
            parent: 'promo',
            url: '/{promoId}/delete',
            params : { promoId: null },
            data: {
                authorities: ['ADMIN', 'USER', 'SUPER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/promo/promo-delete-dialog.html',
                    controller: 'PromoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Promo',  function(Promo) {
                             return Promo.get($stateParams.promoId)
                             .then(function(response){
                                return response.data;
                            })
                        }]
                    }
                }).result.then(function() {
                    $state.go('promo', null, { reload: 'promo' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
