import { Controller } from "../_snowpack/pkg/stimulus.js";
import { connectStreamSource, disconnectStreamSource } from "../_snowpack/pkg/@hotwired/turbo.js";

export default class ConnectSseController extends Controller {
  static targets = ["toggle"];

  connected = false;
  eventSource = undefined;

  toggle() {
    if (this.connected) {
      disconnectStreamSource(this.eventSource);
      this.eventSource.close();
      this.eventSource = undefined;
      this.connected = false;
      this.toggleTarget.innerText = "Attach SSE Stream";
    } else {
      this.eventSource = new EventSource(
        `http://${window.location.host}/turbo-sse`
      );
      connectStreamSource(this.eventSource);
      this.connected = true;
      this.toggleTarget.innerText = "Detach SSE Stream";
    }
  }
}
