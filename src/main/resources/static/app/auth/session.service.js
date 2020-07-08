(function (angular) {

  function sessionService($log, localStorage){

    // Instantiate data when service
    // is loaded
    this._token = localStorage.getItem('token');
    this._langKey = localStorage.getItem('langKey');
    this._annee = angular.fromJson(localStorage.getItem('annee'));

    this._previousState = angular.fromJson(localStorage.getItem('previousState'));
    this._destinationState = angular.fromJson(localStorage.getItem('destinationState'));
    this._destinationStateParams = angular.fromJson(localStorage.getItem('destinationStateParams'));
    this._previousUrl = localStorage.getItem('previousUrl');

    this.getToken = function(){
      return this._token;
    };

    this.setToken = function(token){
      this._token = token;
      localStorage.setItem('token', token);
      return this;
    };

    this.getAnnee = function(){
      return this._annee;
    };

    this.getAnneeId = function(){
      return this._annee.id;
    };

    this.setAnnee = function(annee){
      this._annee = annee;
      localStorage.setItem('annee', angular.toJson(annee));
      return this;
    };

    this.getLangKey = function(){
        if(this._langKey)
        return this._langKey;
         return "fr";
    };

    this.setLangKey = function(langKey){
      this._langKey = langKey;
      localStorage.setItem('langKey', langKey);
      return this;
    };

    this.getCurrency = function(){
        if(this._store)
        return this._store.currency;
        return "Fcfa";
    };

    this.getCountry = function(){
        if(this._store){
            if(this._langKey == "en")
                return this._store.countryNameEn;
            else
                return this._store.countryName;
        }
        return "Cameroun";
    };

    this.getPreviousState = function(){
          return this._previousState;
    };

    this.setPreviousState = function(previousState){
      this._previousState = previousState;
      localStorage.setItem('previousState', angular.toJson(previousState));
      return this;
    };

    this.getDestinationState = function(){
          if(this._destinationState){
              return this._destinationState.name;
          }
          return null;
    };

    this.setDestinationState = function(destinationState){
      this._destinationState = destinationState;
      localStorage.setItem('destinationState', angular.toJson(destinationState));
      return this;
    };

    this.getDestinationStateParams = function(){
      if(this._destinationStateParams){
          return this._destinationStateParams;
      }
      return null;
    };

    this.setDestinationStateParams = function(destinationStateParams){
      this._destinationStateParams = destinationStateParams;
      localStorage.setItem('destinationStateParams', angular.toJson(destinationStateParams));
      return this;
    };

    this.getPreviousUrl = function(){
          return this._previousUrl;
    };

    this.setPreviousUrl = function(previousUrl){
      this._previousUrl = previousUrl;
      localStorage.setItem('previousUrl', previousUrl);
      return this;
    };

    this.getLangKey = function(){
      return this._langKey;
    };

     /**
     * Destroy session
     */
     this.destroy = function destroy(){
        this.setToken('');
     };

  }

  // Inject dependencies
  sessionService.$inject = ['$log', 'localStorage'];

  // Export
  angular
    .module('myApp')
    .service('session', sessionService);

})(angular);