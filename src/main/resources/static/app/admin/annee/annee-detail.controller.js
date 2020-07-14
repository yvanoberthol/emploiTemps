(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('AnneeDetailController', AnneeDetailController);

    AnneeDetailController.$inject = ['$scope', '$stateParams', 'previousState', 'entity', 'Annee', 'Upload'];

    function AnneeDetailController($scope, $stateParams, previousState, entity, Annee, Upload) {
        var vm = this;
        vm.annee = entity;

        vm.previousState = previousState.name;
        vm.saveError= null;
        vm.saveSuccess = null;

        console.log(vm.annee);

        vm.images = new Array(2);
        vm.edited = new Array(2);
        vm.labels = ["Logo", "Cachet"];

        if(vm.annee.id){
            Annee.getImage(vm.annee.id, "logo")
            .then(function(response) {
                var image = arrayBufferToBase64(response.data);
                if(image)
                vm.images[0] = image;
            })
            Annee.getImage(vm.annee.id, "cachet")
            .then(function(response) {
                var image = arrayBufferToBase64(response.data);
                if(image)
                vm.images[1] = image;
            })
        }


        vm.addImage = function(file, index){
            if(file){
                Upload.resize(file, {width: 1200, quality: 0.9}).then(function(image){
                   vm.images[index] = image;
                   vm.edited[index] = true;
                })
            }
        };


        vm.save = save;
        function save () {
            vm.isSaving = true;
            if (vm.annee.id !== null) {
                Annee.update(vm.annee, vm.images[0], vm.images[1], onSaveSuccess, onSaveError);
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
