(function() {
    'use strict';

    angular
        .module('myApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('studentCard', {
            parent: 'app',
            url: '/student-cards',
            data: {
                authorities: ['ADMIN', 'SUPER', 'USER'],
                pageTitle: 'myApp.studentCard.home.title'
            },
            templateUrl: 'app/entities/student-card/student-cards.html',
            controller: 'StudentCardController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('studentCard');
                    $translatePartialLoader.addPart('product');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('studentCard-detail', {
            parent: 'admin',
            url: '/student-card/{studentCardId}',
            params : { studentCardId: null },
            data: {
                authorities: ['ADMIN', 'SUPER', 'USER'],
                pageTitle: 'myApp.studentCard.detail.title'
            },
            templateUrl: 'app/entities/student-card/student-card-detail.html',
            controller: 'StudentCardDetailController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('studentCard');
                    $translatePartialLoader.addPart('product');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StudentCard',  function($stateParams, StudentCard) {
                     var studentCard = {};
                     return StudentCard.get($stateParams.studentCardId)
                     .then(function(response){
                        studentCard = response.data;
                        return response.data;
                    })
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'studentCard',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('studentCard-detail.edit', {
            parent: 'studentCard-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ADMIN', 'SUPER', 'USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/student-card/student-card-dialog.html',
                    controller: 'StudentCardDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: ['StudentCard',  function(StudentCard) {
                             var studentCard = {};
                              return StudentCard.get($stateParams.studentCardId)
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
        .state('studentCard.new', {
            //url: '/new',
            data: {
                authorities: ['ADMIN', 'SUPER', 'USER']
            },
            onEnter: ['$state', '$uibModal', function($state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/student-card/student-card-dialog.html',
                    controller: 'StudentCardDialogController',
                    controllerAs: 'vm',
                    backdrop: 'false',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null,
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('studentCard', null, { reload: 'studentCard' });
                }, function() {
                    $state.go('studentCard');
                });
            }]
        })
        .state('studentCard.edit', {
            parent: 'studentCard',
            url: '/{studentCardId}/edit',
            params : { studentCardId: null },
            data: {
                authorities: ['ADMIN', 'SUPER', 'USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/student-card/student-card-dialog.html',
                    controller: 'StudentCardDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StudentCard',  function(StudentCard) {
                             var studentCard = {};
                             return StudentCard.get($stateParams.studentCardId)
                             .then(function(response){
                                return response.data;
                            })
                        }]
                    }
                }).result.then(function() {
                    $state.go('studentCard', null, { reload: 'studentCard' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('studentCard.delete', {
            parent: 'studentCard',
            url: '/{studentCardId}/delete',
            params : { studentCardId: null },
            data: {
                authorities: ['ADMIN', 'SUPER', 'USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/student-card/student-card-delete-dialog.html',
                    controller: 'StudentCardDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StudentCard',  function(StudentCard) {
                             return StudentCard.get($stateParams.studentCardId)
                             .then(function(response){
                                return response.data;
                            })
                        }]
                    }
                }).result.then(function() {
                    $state.go('studentCard', null, { reload: 'studentCard' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
