package app.rest;

import javax.ws.rs.core.Response;

import com.google.gson.JsonElement;

public class StandardResponse {
    private Response.Status status;
    private String message;
    private JsonElement data;
     
    public StandardResponse(Response.Status status) {
        this.status = status;
    }

    /*public StandardResponse(Response.Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public StandardResponse(Response.Status status, JsonElement data) {
        this.status = status;
        this.data = data;
    }*/
    public StandardResponse withMessage(String msg) {
        this.message = msg;
        return this;
    }
    public StandardResponse withData(JsonElement data) {
        this.data = data;
        return this;
    }

    public Response.Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public JsonElement getData() {
        return data;
    }

}
