(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('SmsDeleteController',SmsDeleteController);

    SmsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Sms'];

    function SmsDeleteController($uibModalInstance, entity, Sms) {
        var vm = this;

        vm.sms = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Sms.delete(id)
            .then(function(response){
                onSaveSuccess(response);
            }, function(){
                onSaveError();
            });
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
