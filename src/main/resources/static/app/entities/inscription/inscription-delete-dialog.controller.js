(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('InscriptionDeleteController',InscriptionDeleteController);

    InscriptionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Inscription'];

    function InscriptionDeleteController($uibModalInstance, entity, Inscription) {
        var vm = this;

        vm.inscription = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (studentId, promoId) {
            Inscription.delete(studentId, promoId)
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
