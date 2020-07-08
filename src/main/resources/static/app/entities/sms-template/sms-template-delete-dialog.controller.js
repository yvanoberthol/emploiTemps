(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('SmsTemplateDeleteController',SmsTemplateDeleteController);

    SmsTemplateDeleteController.$inject = ['$uibModalInstance', 'entity', 'SmsTemplate'];

    function SmsTemplateDeleteController($uibModalInstance, entity, SmsTemplate) {
        var vm = this;

        vm.smsTemplate = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            if(vm.smsTemplate.deletable){
                SmsTemplate.delete(id)
                .then(function(response){
                    onSaveSuccess(response);
                }, function(){
                    onSaveError();
                });
            }
        }

        function onSaveSuccess (response) {
            $uibModalInstance.close(response);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
    }
})();
