(function() {
    'use strict';

    angular
        .module('myApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('contact', {
            parent: 'app',
            url: '/contacts',
            data: {
                authorities: ['ADMIN', 'USER'],
                pageTitle: 'myApp.contact.home.title'
            },
            params: {group: null },
            templateUrl: 'app/entities/contact/contacts.html',
            controller: 'ContactController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('contact');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('contact-delete', {
            parent: 'user-detail',
            url: '/{contactId}/delete',
            params : { contactId: null },
            data: {
                authorities: ['ADMIN', 'USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contact/contact-delete-dialog.html',
                    controller: 'ContactDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    backdrop: true,
                    resolve: {
                        entity: ['Contact',  function(Contact) {
                            console.log($stateParams.contactId)
                             return Contact.get($stateParams.contactId)
                             .then(function(response){
                                return response.data;
                            })
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-detail', null, { reload: 'user-detail' });
                }, function() {
                    $state.go('^');
                });
            }]
        })

        .state('contact.new', {
            //url: '/new',
            data: {
                authorities: ['ADMIN', 'USER']
            },
            onEnter: ['$state', '$uibModal', function($state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contact/contact-dialog.html',
                    controller: 'ContactDialogController',
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
                    $state.go('contact', null, { reload: 'contact' });
                }, function() {
                    $state.go('contact');
                });
            }]
        })
        .state('contact.edit', {
            parent: 'contact',
            url: '/{contactId}/edit',
            params : { contactId: null },
            data: {
                authorities: ['ADMIN', 'USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contact/contact-dialog.html',
                    controller: 'ContactDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: ['Contact',  function(Contact) {
                             return Contact.get($stateParams.contactId)
                             .then(function(response){
                                return response.data;
                             })
                        }]
                    }
                }).result.then(function() {
                    $state.go('contact', null, { reload: 'contact' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('contact.delete', {
            parent: 'contact',
            url: '/{contactId}/delete',
            params : { contactId: null },
            data: {
                authorities: ['ADMIN', 'USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contact/contact-delete-dialog.html',
                    controller: 'ContactDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Contact',  function(Contact) {
                             return Contact.get($stateParams.contactId)
                             .then(function(response){
                                return response.data;
                            })
                        }]
                    }
                }).result.then(function() {
                    $state.go('contact', null, { reload: 'contact' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
