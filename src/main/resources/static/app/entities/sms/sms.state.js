(function() {
    'use strict';

    angular
        .module('myApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sms', {
            parent: 'app',
            url: '/sms',
            data: {
                authorities: ['ADMIN', 'USER'],
                pageTitle: 'myApp.sms.home.title'
            },
            templateUrl: 'app/entities/sms/smss.html',
            controller: 'SmsController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sms');
                    $translatePartialLoader.addPart('product');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('sms.new', {
            //url: '/new',
            data: {
                authorities: ['ADMIN', 'USER']
            },
            onEnter: ['$state', '$uibModal', function($state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sms/sms-dialog.html',
                    controller: 'SmsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'false',
                    size: 'md',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                nbPages: 1,
                                message: "",
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sms', null, { reload: 'sms' });
                }, function() {
                    $state.go('sms');
                });
            }]
        })
        .state('sms-detail.edit', {
            parent: 'sms-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ADMIN', 'USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sms/sms-dialog.html',
                    controller: 'SmsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: ['Sms',  function(Sms) {
                             var sms = {};
                              return Sms.get($stateParams.smsId)
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
        .state('sms.edit', {
            parent: 'sms',
            url: '/{smsId}/edit',
            params : { smsId: null },
            data: {
                authorities: ['ADMIN', 'USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sms/sms-dialog.html',
                    controller: 'SmsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Sms',  function(Sms) {
                                console.log("in state")
                             return Sms.get($stateParams.smsId)
                             .then(function(response){
                                console.log(response)
                                return response.data;
                            })
                        }]
                    }
                }).result.then(function() {
                    $state.go('sms', null, { reload: 'sms' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sms.delete', {
            parent: 'sms',
            url: '/{smsId}/delete',
            params : { smsId: null },
            data: {
                authorities: ['ADMIN', 'USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sms/sms-delete-dialog.html',
                    controller: 'SmsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Sms',  function(Sms) {
                            console.log($stateParams.smsId)
                             return Sms.get($stateParams.smsId)
                             .then(function(response){
                                return response.data;
                            })
                        }]
                    }
                }).result.then(function() {
                    $state.go('sms', null, { reload: 'sms' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
