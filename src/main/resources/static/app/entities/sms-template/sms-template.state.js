(function() {
    'use strict';

    angular
        .module('myApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('smsTemplate', {
            parent: 'app',
            url: '/templates',
            data: {
                authorities: ['ADMIN', 'USER'],
                pageTitle: 'myApp.smsTemplate.home.title'
            },
            templateUrl: 'app/entities/sms-template/sms-templates.html',
            controller: 'SmsTemplateController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('smsTemplate');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('smsTemplate-detail', {
            parent: 'admin',
            url: '/sms-template/{smsTemplateId}',
            params : { smsTemplateId: null },
            data: {
                authorities: ['ADMIN'],
                pageTitle: 'myApp.smsTemplate.detail.title'
            },
            templateUrl: 'app/entities/sms-template/sms-template-detail.html',
            controller: 'SmsTemplateDetailController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('smsTemplate');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SmsTemplate',  function($stateParams, SmsTemplate) {
                     var smsTemplate = {};
                     return SmsTemplate.get($stateParams.smsTemplateId)
                     .then(function(response){
                        smsTemplate = response.data;
                        return response.data;
                    })
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'smsTemplate',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('smsTemplate-detail.edit', {
            parent: 'smsTemplate-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sms-template/sms-template-dialog.html',
                    controller: 'SmsTemplateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: ['SmsTemplate',  function(SmsTemplate) {
                             var smsTemplate = {};
                              return SmsTemplate.get($stateParams.smsTemplateId)
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
        .state('smsTemplate.new', {
            //url: '/new',
            data: {
                authorities: ['ADMIN', 'USER']
            },
            onEnter: ['$state', '$uibModal', function($state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sms-template/sms-template-dialog.html',
                    controller: 'SmsTemplateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'false',
                    size: 'md',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('smsTemplate', null, { reload: 'smsTemplate' });
                }, function() {
                    $state.go('smsTemplate');
                });
            }]
        })
        .state('smsTemplate.edit', {
            parent: 'smsTemplate',
            url: '/{smsTemplateId}/edit',
            params : { smsTemplateId: null },
            data: {
                authorities: ['ADMIN', 'USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sms-template/sms-template-dialog.html',
                    controller: 'SmsTemplateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: ['SmsTemplate',  function(SmsTemplate) {
                                console.log("in state")
                             return SmsTemplate.get($stateParams.smsTemplateId)
                             .then(function(response){
                                console.log(response)
                                return response.data;
                            })
                        }]
                    }
                }).result.then(function() {
                    $state.go('smsTemplate', null, { reload: 'smsTemplate' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('smsTemplate.delete', {
            parent: 'smsTemplate',
            url: '/{smsTemplateId}/delete',
            params : { smsTemplateId: null },
            data: {
                authorities: ['ADMIN', 'USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sms-template/sms-template-delete-dialog.html',
                    controller: 'SmsTemplateDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SmsTemplate',  function(SmsTemplate) {
                            console.log($stateParams.smsTemplateId)
                             return SmsTemplate.get($stateParams.smsTemplateId)
                             .then(function(response){
                                return response.data;
                            })
                        }]
                    }
                }).result.then(function() {
                    $state.go('smsTemplate', null, { reload: 'smsTemplate' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
