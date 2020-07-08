(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('SmsTemplateDialogController', SmsTemplateDialogController);

    SmsTemplateDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity',
                                                'SmsTemplate', 'Sexe', '$filter', 'session'];

    function SmsTemplateDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity,
                                                SmsTemplate, Sexe, $filter, session) {
        var vm = this;
        vm.smsTemplate = entity;
        console.log(vm.smsTemplate);

        vm.smsTemplateExist = true;

        vm.sexes = [];
        Sexe.getAll()
        .then(function(response){
            response.data.forEach(function(sexe) {
               vm.sexes.push({name: sexe.name, value:sexe.value})
            });
        })

        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(0)>input').focus();
        });

        function clear () {
             $uibModalInstance.dismiss('cancel');
         }

        function save () {
            vm.isSaving = true;
            console.log(vm.smsTemplate)
            if (vm.smsTemplate.id !== null) {
                SmsTemplate.update(vm.smsTemplate, onSaveSuccess, onSaveError);
            } else {
                SmsTemplate.save(vm.smsTemplate, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myApp:smsTemplateUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.productChanged =  function(){
            if(vm.smsTemplate.productId){
                vm.product =  $filter('filter')(vm.products, {id: vm.smsTemplate.productId})[0];
                vm.smsTemplate.prixSmsTemplate = vm.product.price;
            }
        }

        function arrayBufferToBase64(buffer) {
           var binary = '';
           var bytes = new Uint8Array(buffer);
           var len = bytes.byteLength;
           for (var i = 0; i < len; i++) {
             binary += String.fromCharCode(bytes[i]);
           }
           return window.btoa(binary);
        }

    }
})();
