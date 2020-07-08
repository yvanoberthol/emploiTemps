(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('ContactDialogController', ContactDialogController);

    ContactDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Contact', 'Group', 'Civilite'];

    function ContactDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Contact, Group, Civilite) {
        var vm = this;
        vm.contact = entity;
        vm.clear = clear;
        vm.save = save;

        vm.civilites = [];
        /*Civilite.getAll()
        .then(function(response){
            response.data.forEach(function(civilite) {
               vm.civilites.push({name: civilite.name, value:civilite.value})
            });
        })*/

        vm.groups = [];
        Group.getAll( 0,999,"name","asc")
        .then(function(response){
           vm.groups = response.data.content;
           console.log(vm.groups)
        })

        $timeout(function (){
            angular.element('.form-group:eq(0)>input').focus();
        });

        function clear () {
             $uibModalInstance.dismiss('cancel');
         }

        function save () {
            console.log(vm.contact)
            vm.isSaving = true;
            if (vm.contact.id !== null) {
                Contact.update(vm.contact, onSaveSuccess, onSaveError);
            } else {
                Contact.save(vm.contact, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
    }
})();
