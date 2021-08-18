import { Controller } from "../_snowpack/pkg/stimulus.js";
import { connectStreamSource, disconnectStreamSource } from "../_snowpack/pkg/@hotwired/turbo.js";

export default class ConnectWebsocketController extends Controller {
  static targets = ["toggle"];
  static values = { connected: Boolean };

  eventSource = undefined;

  toggleStream() {
    if (this.connectedValue) {
      disconnectStreamSource(this.eventSource);
      this.eventSource.close();
      this.eventSource = undefined;
      this.connectedValue = false;
      this.toggleTarget.innerText = "Attach WS Stream";
    } else {
      this.eventSource = new WebSocket(
        `ws://${window.location.host}/stream-updates`
      );
      connectStreamSource(this.eventSource);
      this.connectedValue = true;
      this.toggleTarget.innerText = "Detach WS Stream";
    }
  }
}
