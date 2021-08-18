import { Application } from "../_snowpack/pkg/stimulus.js";

import ConnectSseController from "./connect-sse-controller.js";
import ConnectWebsocketController from "./connect-websocket-controller.js";

const application = Application.start();
application.register("connect-websocket", ConnectWebsocketController);
application.register("connect-sse", ConnectSseController);

console.log("Stimulus controllers registered");
