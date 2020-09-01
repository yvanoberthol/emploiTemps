(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('InscriptionDetailController', InscriptionDetailController);

    InscriptionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity',
                                    'session', 'Promo',  'Sexe', 'Inscription', 'Upload', '$filter'];

    function InscriptionDetailController($scope, $rootScope, $stateParams, previousState, entity,
                                    session, Promo, Sexe, Inscription, Upload, $filter) {
        var vm = this;
        vm.inscription = entity;
        vm.save = save;
        console.log(vm.inscription)

        vm.previousState = previousState.name;
        vm.selected = 0;
        vm.selectedTab = 0;
        vm.saveError= null;
        vm.saveSuccess = null;

         Promo.getByAnnee(session.getAnneeId())
            .then(function(response){
                vm.promos = response.data;
        })

        vm.sexes = [];
        Sexe.getAll()
        .then(function(response){
            response.data.forEach(function(sexe) {
               vm.sexes.push({name: sexe.name, value:sexe.value})
            });
        })

        vm.getSolde = function(index){
            if(index > 0){
                return (vm.inscription.student.mouvements[index].amount + vm.getSolde(index-1));
            }
            else return vm.inscription.student.mouvements[index].amount;
        }


        vm.tabSelected = function(index){
            vm.selectedTab = index;
            vm.ligneTrimestre = vm.inscription.student.notes[index];
            vm.saveError= null;
            vm.saveSuccess = null;
        }
        vm.tabSelected(0);

        var key = Object.keys(vm.inscription.student.notes)[0];
        var matieres = vm.inscription.student.notes[key]
        vm.tabSelected(0, matieres);


        vm.getNote = function(sequenceId, ligneMatiere) {
         var studentNotes =  $filter('filter')(ligneMatiere.notes, {sequenceId: sequenceId});
         if(studentNotes.length){
            return studentNotes[0].note;
         }
        }

        function save () {
            vm.isSaving = true;
            console.log(vm.inscription);
            if(vm.file){
                /*Upload.resize(vm.file, {width: 400, height: 400, quality: 0.8})
                .then(function(image){
                    Inscription.update(vm.inscription, image, onSaveSuccess, onSaveError);
                })*/
                Inscription.update(vm.inscription, vm.file, onSaveSuccess, onSaveError);
            }
            else{
                Inscription.update(vm.inscription, null, onSaveSuccess, onSaveError);
            }

        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            vm.saveError= false;
            vm.saveSuccess = true;
        }

        function onSaveError () {
            vm.isSaving = false;
            vm.saveError= true;
            vm.saveSuccess = false;
        }

        if(vm.inscription.image)
        vm.selectedFile = 'data:image/jpeg;base64,' + vm.inscription.image;

        vm.addImage = function(){
            if(vm.file)
            vm.selectedFile = vm.file;
        };

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
