(function() {
    'use strict';

    angular
        .module('myApp')
        .factory('SmsDialogService', SmsDialogService);

    SmsDialogService.$inject = ['$uibModal', '$rootScope', 'auth'];

    function SmsDialogService ($uibModal, $rootScope, auth) {
        var service = {
            open: open,
            openGroup: openGroup
        };

        var modalInstance = null;
        var resetModal = function () {
            modalInstance = null;
        };
        return service;

        function open (obj) {
            if (modalInstance !== null) return;

            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/entities/sms/sms-dialog.html',
                controller: 'SmsDialogController',
                controllerAs: 'vm',
                backdrop: 'false',
                size: 'md',
                resolve: {
                    entity: function () {
                        if(obj)
                        var recipient = obj.phone;
                        return {
                            recipients: recipient,
                            sender: auth.getSenderId(),
                            group: false,
                            nbPages: 1,
                            message: "",
                            id: null
                        };
                    },
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('sms');
                        return $translate.refresh();
                    }]
                }
            });

            modalInstance.result.then(
                resetModal,
                resetModal
            );
        }

        function openGroup (selected) {
            if (modalInstance !== null) return;
            console.log(selected)
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/entities/sms/sms-dialog.html',
                controller: 'SmsDialogController',
                controllerAs: 'vm',
                backdrop: 'false',
                size: 'lg',
                resolve: {
                    entity: function () {
                        var recipients = [];
                        selected.forEach(function(recipient){
                            if(recipient.phone)
                            recipients.push(recipient.phone)
                        })
                        return {
                            recipients: recipients.join(", "),
                            sender: auth.getSenderId(),
                            group: true,
                            nbRecipients: recipients.length,
                            nbPages: 1,
                            message: "",
                            id: null
                        };
                    },
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('sms');
                        return $translate.refresh();
                    }]
                }
            });

            modalInstance.result.then(
                resetModal,
                resetModal
            );
        }
    }
})();
