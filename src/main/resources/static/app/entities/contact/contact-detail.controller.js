(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('ContactDetailController', ContactDetailController);

    ContactDetailController.$inject = ['$scope', '$rootScope', 'auth', '$stateParams', 'previousState', 'entity',
                                        'Contact', 'Location', 'NgTableParams', '$uibModal', 'leafletData', '$timeout'];

    function ContactDetailController($scope, $rootScope, auth, $stateParams, previousState, entity,
                                        Contact, Location, NgTableParams, $uibModal, leafletData, $timeout) {
        var vm = this;
        vm.loading = true;
        vm.contact = entity;
        vm.previousState = previousState.name;

        var pattern = "YYYY-MM-DD HH:mm";
        var patternDateTime = "YYYY-MM-DD HH:mm";
        var patternDate = "YYYY-MM-DD";
        var patternTime = "HH:mm";

        if(!$rootScope.view)
        $rootScope.view = "list";

        //default range
        if(!$rootScope.dateFrom | !$rootScope.dateTo){
            $rootScope.dateFrom = moment().startOf('day').format(patternDateTime);
            $rootScope.dateTo = moment().endOf('day').format(patternDateTime);
            vm.currentDateFrom = moment().startOf('day').toDate();
            vm.currentDateTo = moment().endOf('day').toDate();
        }
        else{
          vm.currentDateFrom = moment($rootScope.dateFrom).toDate();
          vm.currentDateTo = moment($rootScope.dateTo).toDate();
        }

        vm.locations = [];
        vm.locationsFull = [];
        vm.paths = [];
        vm.markers = [];
        vm.sortBy = "date";
        vm.direction = "desc";

        var initialParams = {
           count: 30,
           sorting: { 'date': "desc" }
        };

        loadLocationsFull();
        vm.loadLocationsFull = loadLocationsFull;
        function loadLocationsFull(){
            Location.getAllByContact(0, 9999, "date", "asc", vm.contact.id, $rootScope.dateFrom, $rootScope.dateTo)
           .then(function(response){
               vm.paths = [];
               vm.markers = [];
               vm.locationsFull = response.data.content;
               var coords = [];
               vm.locationsFull.forEach(function(loc, index){
                   if (loc.lat && loc.lng) {
                        coords.push([loc.lat, loc.lng]);
                        var marker = createMarker(loc, index)
                        vm.markers.push(marker);
                   }
               })
               var path = {
                   layer: 'route',
                   type: "polyline",
                   color: 'blue',
                   weight: 8,
                   latlngs: coords,
               }
               vm.paths.push(path);
               console.log(vm.paths)
           })
        }

        function createMarker(loc, index) {
            var marker = {};
            marker.icon = {
                iconUrl: 'images/dot_green.png',
                iconSize:     [20, 20],
                iconAnchor:   [10, 10],
            }

            marker.message = loc.date + "<br>";
            marker.layer = 'locations';
            marker.lat = loc.lat;
            marker.lng = loc.lng;
            marker.focus = false;
            marker.id = loc.id;
            return marker;
        }

        vm.tableParams = new NgTableParams(initialParams, {
            //counts: [5,10,25,50,100],
            counts: [],
            paginationMaxBlocks: 10,
            paginationMinBlocks: 2,
            getData: function(params) {
                if(params.sorting()){
                    //console.log(params.sorting());
                    for(var sortBy in params.sorting()){
                      vm.sortBy = sortBy;
                    }
                }

                return Location.getAllByContact(params.page()-1, params.count(), vm.sortBy, vm.direction,
                                       vm.contact.id, $rootScope.dateFrom, $rootScope.dateTo)
                .then(function(response){
                      params.total(response.data.totalElements);
                      vm.locations = response.data.content;
                      vm.loading = false;
                      return vm.locations;
                })
            }
        });

        vm.closeAlert = function() {
           vm.errorDateFromNotValid = null;
           vm.errorDateToNotValid = null;
           vm.errorToBeforeFrom = null;
        };

        vm.openDateModal = function () {
            //vm.dateFrom = moment($rootScope.dateFrom).format('DDMMYYYY HH:mm');
            //vm.dateTo = moment($rootScope.dateTo).format('DDMMYYYY HH:mm')
            vm.dateFrom = moment($rootScope.dateFrom).format('DDMMYYYY');
            vm.hourFrom = moment($rootScope.dateFrom).format('HH:mm');
            vm.dateTo = moment($rootScope.dateTo).format('DDMMYYYY');
            vm.hourTo = moment($rootScope.dateTo).format('HH:mm');

            vm.modalInstance = $uibModal.open({
                templateUrl: 'dateModal.html',
                scope: $scope, //passed current scope to the modal
                size: 'md',
                backdrop : true
            });
        };

        vm.closeModal = function() {
            vm.closeAlert();
            vm.modalInstance.dismiss('cancel');
        };

        vm.search = search;
        function search() {
            vm.tableParams.page(1);
            vm.tableParams.reload();
            loadLocationsFull();
        }

        vm.searchByDate = function(){
           vm.closeAlert();
           var dateFrom = moment(vm.dateFrom + " " + vm.hourFrom , 'DDMMYYYY HH:mm');
           var dateTo = moment(vm.dateTo + " " + vm.hourTo , 'DDMMYYYY HH:mm');
           console.log(dateFrom)
           console.log(dateTo)

           /*var dateFrom = moment(vm.dateFrom, 'DDMMYYYY');
           var hourFrom = moment(vm.dateFrom, 'HH:mm');
           var dateTo = moment(vm.dateTo, 'DDMMYYYY');
           var hourTo = moment(vm.hourTo, 'HH:mm');*/

           if (!dateFrom.isValid()) {
              vm.errorDateFromNotValid = true;
           }
           else if (!dateTo.isValid()) {
              vm.errorDateToNotValid = true;
           }
           else if (dateFrom.isValid() && dateTo.isValid()) {
               if(dateTo.isBefore(dateFrom)){
                   vm.errorToBeforeFrom = true;
               }
               else{
                   vm.closeAlert();
                   $rootScope.dateFrom = dateFrom.format(patternDateTime);
                   $rootScope.dateTo = dateTo.format(patternDateTime);
                   vm.currentDateFrom = dateFrom.toDate();
                   vm.currentDateTo = dateTo.toDate();
                   vm.search();
                   vm.closeModal();
               }
           }
       };

        vm.layers = {
           baselayers: {
               osm: {
                   name: 'OpenStreetMap',
                   type: 'xyz',
                   //url: 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
                   //url: 'https://makeitmap.com/osmtiles/{z}/{x}/{y}.png',
                   url: auth.getTiles(),
                   layerOptions: {
                       showOnSelector: false
                   }
               }
           },
           overlays: {
               locations: {
                   type: 'group',
                   name: 'locations',
                   visible: true,
                   layerOptions: {
                       showOnSelector: false
                   }
               },
               route: {
                  type: 'group',
                  name: 'route',
                  visible: true,
                  layerOptions: {
                      showOnSelector: false
                  }
               }
           }
        }

        vm.showMap = function(){
            $rootScope.view = "map";
            leafletData.getMap("route_map")
            .then(function (map) {
               $timeout(function() {map.invalidateSize()}, 200);
            });
        }

        vm.refreshMap = refreshMap;
        function refreshMap(){
            leafletData.getMap("route_map")
            .then(function (map) {
               $timeout(function() {map.invalidateSize()}, 200);

               if(vm.contact.lat && vm.contact.lng){
                map.setView(new L.latLng(vm.contact.lat, vm.contact.lng), 11);
               }
               else if(auth.getLat() && auth.getLng())
                map.setView(new L.latLng(auth.getLat(), auth.getLng()), 11);
                //map.addControl(new L.Control.Fullscreen());
            });
        }
        vm.refreshMap();

        vm.openLocationModal = function (location) {
            vm.currentLocation = location;
            angular.extend($scope, {
                center: {
                    lat: location.lat,
                    lng: location.lng,
                    zoom: 12
                },
                markers: {
                    marker: {
                        lat: location.lat,
                        lng: location.lng,
                        icon: {
                            type: 'awesomeMarker',
                            prefix: 'fa',
                            icon: 'car',
                            markerColor: 'darkblue'
                        },
                        draggable: false
                    }
                },
                layers : {
                    baselayers: {
                        osm: {
                            name: 'OpenStreetMap',
                            type: 'xyz',
                            //url: 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
                            //url: 'https://makeitmap.com/osmtiles/{z}/{x}/{y}.png',
                            url : auth.getTiles(),
                            layerOptions: {
                                showOnSelector: false
                            }
                        }
                    }
                }
            });

            leafletData.getMap("loc_map")
            .then(function(map) {
                $timeout(function() {map.invalidateSize()}, 200);
            });
            vm.modalInstance = $uibModal.open({
                templateUrl: 'app/entities/contact/contact-location-dialog.html',
                scope: $scope,
                size: 'md',
                backdrop : true
            });
        };
    }
})();
