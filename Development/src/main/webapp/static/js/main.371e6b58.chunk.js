(this["webpackJsonptaut-app"]=this["webpackJsonptaut-app"]||[]).push([[0],{21:function(e,t,n){},26:function(e,t,n){e.exports=n(38)},31:function(e,t,n){},38:function(e,t,n){"use strict";n.r(t);var a=n(0),s=n.n(a),o=n(23),r=n.n(o),c=(n(31),n(15)),i=n(1),l=n(2),u=n(4),p=n(3),m=n(5),h=n(11),g=n(10),E=(n(21),n(14)),f=function(e){function t(e){var n;return Object(i.a)(this,t),(n=Object(u.a)(this,Object(p.a)(t).call(this,e))).state={value:""},n.handleChange=n.handleChange.bind(Object(E.a)(n)),n.handleLogin=n.handleLogin.bind(Object(E.a)(n)),n.handleCreate=n.handleCreate.bind(Object(E.a)(n)),n}return Object(m.a)(t,e),Object(l.a)(t,[{key:"handleChange",value:function(e){this.setState({value:e.target.value})}},{key:"handleLogin",value:function(e){e.preventDefault();var t=JSON.stringify({type:"USER_SERVICE",contentType:"LOGIN",content:this.state.value});console.log("Logging in as %s",this.state.value),this.props.send(t)}},{key:"handleCreate",value:function(e){e.preventDefault();var t=JSON.stringify({type:"USER_SERVICE",contentType:"USER_CREATE",content:this.state.value});console.log("Creating new user: %s",this.state.value),this.props.send(t)}},{key:"render",value:function(){return null!==this.props.username?s.a.createElement(g.a,{to:"/chat"}):s.a.createElement("div",{className:"section is-fullheight"},s.a.createElement("div",{className:"container"},s.a.createElement("div",{className:"column is-4 is-offset-4"},s.a.createElement("div",{className:"box"},s.a.createElement("form",{onSubmit:this.handleLogin},s.a.createElement("div",{className:"field"},s.a.createElement("label",{className:"label"},"User Name"),s.a.createElement("div",{className:"control"},s.a.createElement("input",{className:"input",type:"text",name:"username",autoComplete:"off",onChange:this.handleChange}))),s.a.createElement("div",{className:"buttons is-grouped"},s.a.createElement("button",{type:"button",className:"button is-block is-info is-fullwidth",onClick:this.handleLogin},"Login"),s.a.createElement("button",{type:"button",className:"button is-block is-info is-fullwidth",onClick:this.handleCreate},"Create")))))))}}]),t}(s.a.Component),S=function(e){function t(){var e,n;Object(i.a)(this,t);for(var a=arguments.length,s=new Array(a),o=0;o<a;o++)s[o]=arguments[o];return(n=Object(u.a)(this,(e=Object(p.a)(t)).call.apply(e,[this].concat(s)))).state={display:"TautChat"},n}return Object(m.a)(t,e),Object(l.a)(t,[{key:"componentDidUpdate",value:function(e){if(this.props.username!==e.username){var t="Hi, "+this.props.username+"!";this.setState({display:t})}}},{key:"render",value:function(){return s.a.createElement("nav",{class:"breadcrumb is-centered is-medium has-bullet-separator","aria-label":"breadcrumbs"},s.a.createElement("h1",{class:"title is-2"},this.state.display),s.a.createElement("ul",null,s.a.createElement("li",null,s.a.createElement(h.b,{to:"/"},"Home")),s.a.createElement("li",null,s.a.createElement(h.b,{to:"/chat"},"Chat")),s.a.createElement("li",null,s.a.createElement(h.b,{to:"/about"},"About"))))}}]),t}(a.Component),d=function(e){function t(){return Object(i.a)(this,t),Object(u.a)(this,Object(p.a)(t).apply(this,arguments))}return Object(m.a)(t,e),Object(l.a)(t,[{key:"getStyle",value:function(){return{fontSize:"12px",padding:"30px"}}},{key:"render",value:function(){return s.a.createElement(s.a.Fragment,null,s.a.createElement("div",{style:this.getStyle()},s.a.createElement("p",null,"TautChat was established in 2019 by 4 dashing young professionals looking to expand their careers into the world of software development. They would like to thank to the following people for their support during development:"),s.a.createElement("p",null,"Michael Weintraub"),s.a.createElement("p",null,"Vaibhav Dave"),s.a.createElement("p",null,"Alex Grob")))}}]),t}(a.Component),b=n(13),C=function(e){function t(){var e,n;Object(i.a)(this,t);for(var a=arguments.length,s=new Array(a),o=0;o<a;o++)s[o]=arguments[o];return(n=Object(u.a)(this,(e=Object(p.a)(t)).call.apply(e,[this].concat(s)))).state={content:""},n.onChange=function(e){return n.setState(Object(b.a)({},e.target.name,e.target.value))},n.onSubmit=function(e){e.preventDefault(),n.props.addMessage(n.state.content),n.setState({content:""})},n}return Object(m.a)(t,e),Object(l.a)(t,[{key:"render",value:function(){return s.a.createElement("form",{onSubmit:this.onSubmit},s.a.createElement("input",{className:"input",type:"text",name:"content",placeholder:"Type Message",autoComplete:"off",value:this.state.content,onChange:this.onChange}))}}]),t}(a.Component),v=function(e){function t(){var e,n;Object(i.a)(this,t);for(var a=arguments.length,s=new Array(a),o=0;o<a;o++)s[o]=arguments[o];return(n=Object(u.a)(this,(e=Object(p.a)(t)).call.apply(e,[this].concat(s)))).getStyle=function(e){return n.props.username===e?{fontSize:"20px",textAlign:"right",padding:"10px"}:{fontSize:"20px",textAlign:"left",padding:"10px"}},n.getNameStyle=function(e){return{fontSize:"14px",padding:"1px"}},n.getFontStyle=function(e){switch(e.charAt(0)){case"0":return{color:"#FF0000"};case"1":return{color:"#696969"};case"2":return{color:"#7CFC00"}}},n}return Object(m.a)(t,e),Object(l.a)(t,[{key:"render",value:function(){var e=this.props.message,t=e.from,n=(e.to,e.content),a=e.sentiment;return s.a.createElement("div",{style:this.getStyle(t)},s.a.createElement("p",{style:this.getNameStyle()},t),s.a.createElement("p",{style:this.getFontStyle(a)},n))}}]),t}(a.Component),y=function(e){function t(){var e,n;Object(i.a)(this,t);for(var a=arguments.length,s=new Array(a),o=0;o<a;o++)s[o]=arguments[o];return(n=Object(u.a)(this,(e=Object(p.a)(t)).call.apply(e,[this].concat(s)))).filtered=function(e){var t,a=n.props.messageWith,s=n.props.username;return n.props.groups.includes(a)?(console.log("Filtering Group"),t=e.filter((function(e){return e.to===a}))):(console.log("Filtering Direct"),t=e.filter((function(e){return e.from===s||e.from===a})).filter((function(e){return e.to===s||e.to===a}))),t},n}return Object(m.a)(t,e),Object(l.a)(t,[{key:"render",value:function(){var e=this;return this.filtered(this.props.messages).map((function(t){return s.a.createElement(v,{message:t,filterMessages:e.filterMessages,username:e.props.username,messageWith:e.props.messageWith})}))}}]),t}(a.Component),O=function(e){function t(){var e,n;Object(i.a)(this,t);for(var a=arguments.length,s=new Array(a),o=0;o<a;o++)s[o]=arguments[o];return(n=Object(u.a)(this,(e=Object(p.a)(t)).call.apply(e,[this].concat(s)))).state={messageWith:""},n.onChange=function(e){n.setState(Object(b.a)({},e.target.name,e.target.value),(function(){n.props.setTo(n.state.messageWith)}))},n}return Object(m.a)(t,e),Object(l.a)(t,[{key:"getStyle",value:function(){return{textAlign:"left"}}},{key:"render",value:function(){return s.a.createElement("div",null,s.a.createElement("h1",{class:"subtitle is-3",style:this.getStyle()},this.props.messageWith))}}]),t}(a.Component),R=function(e){function t(){var e,n;Object(i.a)(this,t);for(var a=arguments.length,s=new Array(a),o=0;o<a;o++)s[o]=arguments[o];return(n=Object(u.a)(this,(e=Object(p.a)(t)).call.apply(e,[this].concat(s)))).setMessageWith=function(e){n.props.setTo(e)},n.filtered=function(e){return e.filter((function(e){return e!==n.props.username}))},n}return Object(m.a)(t,e),Object(l.a)(t,[{key:"render",value:function(){var e=this;return this.filtered(this.props.users).map((function(t){return s.a.createElement("li",null,s.a.createElement("button",{class:"button is-white is-fullwidth",onClick:function(){return e.setMessageWith(t)}},t))}))}}]),t}(a.Component),A=function(e){function t(){var e,n;Object(i.a)(this,t);for(var a=arguments.length,s=new Array(a),o=0;o<a;o++)s[o]=arguments[o];return(n=Object(u.a)(this,(e=Object(p.a)(t)).call.apply(e,[this].concat(s)))).setGroup=function(e){n.props.setTo(e)},n}return Object(m.a)(t,e),Object(l.a)(t,[{key:"render",value:function(){var e=this;return this.props.groups.map((function(t){return s.a.createElement("li",null,s.a.createElement("button",{class:"button is-white is-fullwidth",onClick:function(){return e.setGroup(t)}},t))}))}}]),t}(a.Component),U=function(e){function t(){var e,n;Object(i.a)(this,t);for(var a=arguments.length,s=new Array(a),o=0;o<a;o++)s[o]=arguments[o];return(n=Object(u.a)(this,(e=Object(p.a)(t)).call.apply(e,[this].concat(s)))).state={newGroup:""},n.onChange=function(e){return n.setState(Object(b.a)({},e.target.name,e.target.value))},n.onSubmit=function(e){e.preventDefault(),n.props.addGroup(n.state.newGroup),n.setState({newGroup:""})},n}return Object(m.a)(t,e),Object(l.a)(t,[{key:"render",value:function(){return s.a.createElement("form",{onSubmit:this.onSubmit},s.a.createElement("input",{className:"input is-small",type:"text",name:"newGroup",placeholder:"New Group",autoComplete:"off",value:this.state.newGroup,onChange:this.onChange}))}}]),t}(a.Component),j=function(e){function t(){var e,n;Object(i.a)(this,t);for(var a=arguments.length,s=new Array(a),o=0;o<a;o++)s[o]=arguments[o];return(n=Object(u.a)(this,(e=Object(p.a)(t)).call.apply(e,[this].concat(s)))).state={messageWith:"",messages:[]},n.filterMessages=function(){if(n.props.groups.includes(n.state.messageWith)){var e=n.state.messageWith,t=(n.props.username,n.state.messages.filter((function(t){return t.to===e})));n.setState({messages:t})}else{var a=n.state.messageWith,s=n.props.username,o=n.state.messages.filter((function(e){return e.from===s||e.from===a})).filter((function(e){return e.to===s||e.to===a}));n.setState({messages:o})}},n.addToChatBox=function(e){n.setState({messages:[].concat(Object(c.a)(n.state.messages),[e])},(function(){n.filterMessages()}))},n.setTo=function(e){n.setState({messageWith:e})},n.addGroup=function(e){n.props.addGroup(e);var t=JSON.stringify({type:"GROUP_SERVICE",contentType:"GROUP_CREATE",content:e});n.props.send(t)},n.addMessage=function(e){if(n.props.groups.includes(n.state.messageWith))var t=JSON.stringify({type:"BROADCAST_MESSAGE",from:n.props.username,to:n.state.messageWith,contentType:"GROUP_MESSAGE",content:e});else if(n.props.users.includes(n.state.messageWith))t=JSON.stringify({type:"BROADCAST_MESSAGE",from:n.props.username,to:n.state.messageWith,contentType:"ASCII",content:e});n.props.send(t)},n}return Object(m.a)(t,e),Object(l.a)(t,[{key:"componentDidUpdate",value:function(e){var t=this;this.props.queuedMessage!==e.queuedMessage&&this.setState({messages:[].concat(Object(c.a)(this.state.messages),[this.props.queuedMessage])},(function(){console.log(t.state.messages)}))}},{key:"componentDidMount",value:function(){}},{key:"sendSearchForUsernameMessage",value:function(e){var t=JSON.stringify({type:"USER_SERVICE",contentType:"SEARCH_USERS_BY_NAME",content:e});this.props.send(t)}},{key:"render",value:function(){return null===this.props.username?s.a.createElement(g.a,{to:"/"}):s.a.createElement("div",{className:"container"},s.a.createElement("div",{className:"columns is-mobile"},s.a.createElement("div",{className:"column is-narrow"},s.a.createElement("h1",{class:"title"},"Users"),s.a.createElement(s.a.Fragment,null,s.a.createElement(R,{users:this.props.users,setTo:this.setTo,username:this.props.username})),s.a.createElement("h1",{class:"title"},"Groups"),s.a.createElement(s.a.Fragment,null,s.a.createElement(U,{addGroup:this.addGroup}),s.a.createElement(A,{groups:this.props.groups,setTo:this.setTo}))),s.a.createElement("div",{className:"column"},s.a.createElement(s.a.Fragment,null,s.a.createElement(O,{messageWith:this.state.messageWith}),s.a.createElement(y,{messages:this.state.messages,username:this.props.username,messageWith:this.state.messageWith,groups:this.props.groups}),s.a.createElement(C,{addMessage:this.addMessage})))))}}]),t}(a.Component),N={LOGIN:"LOGIN",USER_CREATE:"USER_CREATE",SEARCH_USERS_BY_NAME:"SEARCH_USERS_BY_NAME",FRIEND_USER:"FRIEND_USER"};function _(e){e.contentType==N.LOGIN&&function(e){e.content.includes(T.SUCCESS)?(console.log("SUCCESS: User successfully logged into session."),alert("User successfully logged into session.")):e.content.includes(T.FAILURE)&&(console.log("ERROR: User Name Does not Exist. Please Create User First."),alert("User Name Does not Exist. Please Create User First."))}(e),e.contentType==N.USER_CREATE&&function(e){e.content.includes(T.SUCCESS)?(console.log("User Successfully Created!"),alert("User Successfully Created!")):e.content.includes(T.FAILURE)&&(console.log("ERROR: Could Not Create User Name. Please Try Another Name."),alert("This User Name is already taken. Please Try Another Name."))}(e),e.contentType,N.SEARCH_USERS_BY_NAME}var M={ASCII:"ASCII",GROUP_MESSAGE:"GROUP_MESSAGE"};function k(e){console.log("I got something to use"),console.log(e)}var G,w={USER_SERVICE:"USER_SERVICE",SESSION_SERVICE:"SESSION_SERVICE",GROUP_SERVICE:"GROUP_SERVICE",GROUP_MESSAGE:"GROUP_MESSAGE",DIRECT_MESSAGE:"DIRECT_MESSAGE",BROADCAST_MESSAGE:"BROADCAST_MESSAGE"},T={SUCCESS:"SUCCESS",FAILURE:"FAILURE"};function I(e){console.log("General message router"),console.log(e),e.type==w.BROADCAST_MESSAGE&&(console.log("I'm going to go process this broadcast"),function(e){e.type==w.BROADCAST_MESSAGE?k(e):e.type==w.DIRECT_MESSAGE&&(e.contentType==M.GROUP_MESSAGE||k(e))}(e)),e.from==w.USER_SERVICE&&_(e)}function D(){if(console.log("Checking Socket Connection"),"undefined"===typeof G){console.log("Connecting to Socket");var e=window.location.hostname,t=window.location.pathname;(G=new WebSocket("ws://"+e+t+"chat/")).onopen=function(){console.log("Connection open!"),!0},G.onclose=function(e){console.log("websocket closing. Code:",e),!1},G.onerror=function(e){console.log("Websocket Error"),console.log("Error Code: ",e.data)},G.onmessage=function(e){console.log("Received Message"),I(JSON.parse(e.data))}}}var W=function(e){function t(){var e,n;Object(i.a)(this,t);for(var a=arguments.length,s=new Array(a),o=0;o<a;o++)s[o]=arguments[o];return(n=Object(u.a)(this,(e=Object(p.a)(t)).call.apply(e,[this].concat(s)))).state={ws:"",username:null,active_socket:!1,authenticated:!0,queuedMessage:"",users:[""],groups:["Kitty Lovers","Science Project","Taut Admins"]},n.connect=function(){var e=n.state.ws;n.state.active_socket;if(console.log("Checking Socket Connection"),""===n.state.ws){console.log("Connecting to Socket");var t=window.location.hostname,a=window.location.pathname;console.log(t),console.log(a),e=new WebSocket("ws://"+t+a+"chat/"),n.setState({ws:e}),e.onopen=function(){console.log("Connection open!"),n.setState({active_socket:!0},(function(){n.retrieveAllUsers()}))},e.onclose=function(e){console.log("websocket closing. Code:",e),n.setState({active_socket:!1}),n.setState({ws:""})},e.onerror=function(e){console.log("Websocket Error"),console.log("Error Code: ",e.data)},e.onmessage=function(e){console.log("Received Message"),n.generalMessageRouter(JSON.parse(e.data))}}},n.send=function(e){var t=n.state.ws;n.state.active_socket?t.send(e):(D(),setTimeout((function(){t.send(e)}),500))},n.retrieveAllUsers=function(){var e=JSON.stringify({type:"USER_SERVICE",contentType:"SEARCH_USERS_BY_NAME",content:""});n.send(e)},n.addGroup=function(e){n.setState({groups:[e].concat(Object(c.a)(n.state.groups))})},n.generalMessageRouter=function(e){console.log(e),"BROADCAST_MESSAGE"==e.type&&n.chatMessageRouter(e),"USER_SERVICE"==e.from&&n.userServiveMessageRouter(e)},n.chatMessageRouter=function(e){"BROADCAST_MESSAGE"==e.type?n.processDirectChatMessage(e):"DIRECT_MESSAGE"==e.type&&n.processDirectChatMessage(e)},n}return Object(m.a)(t,e),Object(l.a)(t,[{key:"componentDidMount",value:function(){this.connect()}},{key:"processDirectChatMessage",value:function(e){console.log(e),this.setState({queuedMessage:e})}},{key:"processGroupMessage",value:function(e){console.log(e),this.setState({queuedMessage:e})}},{key:"userServiveMessageRouter",value:function(e){"LOGIN"==e.contentType&&this.processLoginResponse(e),"USER_CREATE"==e.contentType&&this.processUserCreateResponse(e),"SEARCH_USERS_BY_NAME"==e.contentType&&this.processUserSearch(e)}},{key:"processLoginResponse",value:function(e){e.content.includes("SUCCESS")?(this.setState({username:e.to}),console.log(e),console.log("SUCCESS: User successfully logged into session."),this.setState({authenticated:!0})):e.content.includes("FAILURE")&&(alert("User Name Does not Exist. Please Create User First."),console.log("ERROR: User Name Does not Exist. Please Create User First."))}},{key:"processUserCreateResponse",value:function(e){e.content.includes("SUCCESS")?(this.setState({users:[].concat(Object(c.a)(this.state.users),[e.to])}),alert("User successfully created. Please log in to continue"),console.log("User Successfully Created!")):e.content.includes("FAILURE")&&(alert("Could Not Create User Name. Please Try Another Name."),console.log("ERROR: Could Not Create User Name. Please Try Another Name."))}},{key:"processUserSearch",value:function(e){console.log(e.content);var t=e.content.split(",");this.setState({users:t})}},{key:"render",value:function(){var e=this;return s.a.createElement(h.a,{basename:"prattle"},s.a.createElement("div",{className:"App"},s.a.createElement("div",{className:"container"},s.a.createElement(S,{username:this.state.username}),s.a.createElement(g.b,{exact:!0,path:"/",render:function(t){return s.a.createElement(f,Object.assign({},t,{connect:e.connect,send:e.send,username:e.state.username}))}}),s.a.createElement(g.b,{path:"/about",component:d}),s.a.createElement(g.b,{path:"/chat/:messageWith?",render:function(t){return s.a.createElement(j,Object.assign({},t,{connect:e.connect,send:e.send,username:e.state.username,queuedMessage:e.state.queuedMessage,users:e.state.users,groups:e.state.groups,addGroup:e.addGroup}))}}))))}}]),t}(a.Component);n(37);r.a.render(s.a.createElement(W,null),document.getElementById("root"))}},[[26,1,2]]]);
//# sourceMappingURL=main.371e6b58.chunk.js.map