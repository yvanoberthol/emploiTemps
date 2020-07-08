(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('ContactDeleteController',ContactDeleteController);

    ContactDeleteController.$inject = ['$uibModalInstance', 'entity', 'Contact'];

    function ContactDeleteController($uibModalInstance, entity, Contact) {
        var vm = this;

        vm.contact = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Contact.delete(id)
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
