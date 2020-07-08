(function() {
    'use strict';

    angular
        .module('myApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('group', {
            parent: 'app',
            url: '/groups',
            data: {
                pageTitle: 'myApp.group.home.title'
            },
            templateUrl: 'app/entities/group/groups.html',
            controller: 'GroupController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('group');
                    $translatePartialLoader.addPart('contact');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('group-detail.edit', {
            parent: 'group-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ADMIN', 'USER', 'SUPER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/group/group-dialog.html',
                    controller: 'GroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: ['Group',  function(Group) {
                             var group = {};
                              return Group.get($stateParams.groupId)
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
        .state('group.new', {
            //url: '/new',
            data: {
                authorities: ['ADMIN', 'USER', 'SUPER']
            },
            onEnter: ['$state', '$uibModal', 'session', function($state, $uibModal, session) {
                $uibModal.open({
                    templateUrl: 'app/entities/group/group-dialog.html',
                    controller: 'GroupDialogController',
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
                    $state.go('group', null, { reload: 'group' });
                }, function() {
                    $state.go('group');
                });
            }]
        })
        .state('group.edit', {
            parent: 'group',
            url: '/{groupId}/edit',
            params : { groupId: null },
            data: {
                authorities: ['ADMIN', 'USER', 'SUPER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/group/group-dialog.html',
                    controller: 'GroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: ['Group',  function(Group) {
                             var group = {};
                             return Group.get($stateParams.groupId)
                             .then(function(response){
                                return response.data;
                            })
                        }]
                    }
                }).result.then(function() {
                    $state.go('group', null, { reload: 'group' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('group.students', {
            parent: 'group',
            url: '/{groupId}/students',
            params : { groupId: null },
            data: {
                authorities: ['ADMIN', 'USER', 'SUPER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/group/group-students-dialog.html',
                    controller: 'GroupStudentsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Group',  function(Group) {
                             var group = {};
                             return Group.get($stateParams.groupId)
                             .then(function(response){
                                return response.data;
                            })
                        }]
                    }
                }).result.then(function() {
                    $state.go('group', null, { reload: 'group' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('group.delete', {
            parent: 'group',
            url: '/{groupId}/delete',
            params : { groupId: null },
            data: {
                authorities: ['ADMIN', 'USER', 'SUPER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/group/group-delete-dialog.html',
                    controller: 'GroupDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Group',  function(Group) {
                             return Group.get($stateParams.groupId)
                             .then(function(response){
                                return response.data;
                            })
                        }]
                    }
                }).result.then(function() {
                    $state.go('group', null, { reload: 'group' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
