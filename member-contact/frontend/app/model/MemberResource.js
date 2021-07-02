'use strict';

angular.module('memberApp').factory('MemberResource', MemberResource);

MemberResource.$inject = ['$resource'];
function MemberResource($resource) {
  var endpoint = "http://localhost:8080/members";
  return $resource(endpoint, {}, {
    getAll: {
      method: 'GET',
      isArray: true
    },
    getMember: {
      url: endpoint + '/:id',
      method: 'GET',
      isArray: false
    }
  });
}