(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('SmsDialogController', SmsDialogController);

    SmsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'Sms', '$filter',
                                    'entity', 'SmsTemplate'];

    function SmsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, Sms, $filter,
                                    entity, SmsTemplate) {
        var vm = this;
        vm.sms = entity;

        vm.clear = clear;
        vm.save = save;

        SmsTemplate.getAll()
        .then(function(response){
            vm.smsTemplates = response.data;
        });

        $scope.$watch('vm.sms.message', function(newVal, oldVal) {
            vm.sms.nbPages = parseInt(vm.sms.message.length/161) + 1
        });

        function clear () {
             $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            vm.loading = true;
            vm.error = false;

            if(!Array.isArray(vm.sms.recipients))
            vm.sms.recipients = vm.sms.recipients.split(",").map(function(item) {
              return item.trim();
            });

            if (vm.sms.id !== null) {
                Sms.update(vm.sms, onSaveSuccess, onSaveError);
            } else {
                Sms.save(vm.sms, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myApp:smsUpdate', result);
            vm.success = true;
            $timeout(function() { $uibModalInstance.close(result);}, 3000);
            vm.isSaving = false;
            vm.loading = false;
        }

        function onSaveError (error) {
            vm.error = true;
            console.log(error)
            vm.errorMessage = error.data.errorMessage;
            vm.isSaving = false;
            vm.loading = false;
        }

    }
})();
