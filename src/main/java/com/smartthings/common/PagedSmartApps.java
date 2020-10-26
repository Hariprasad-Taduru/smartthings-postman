package com.smartthings.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.smartthings.sdk.client.models.Links;

import io.swagger.annotations.ApiModelProperty;

public class PagedSmartApps {

	@JsonProperty("items")
	  private List<JsonNode> items = null;

	  @JsonProperty("_links")
	  private Links links = null;

	  public PagedSmartApps items(List<JsonNode> items) {
	    this.items = items;
	    return this;
	  }

	  public PagedSmartApps addItemsItem(JsonNode itemsItem) {
	    if (this.items == null) {
	      this.items = new ArrayList<>();
	    }
	    this.items.add(itemsItem);
	    return this;
	  }

	   /**
	   * Get items
	   * @return items
	  **/
	  @ApiModelProperty(value = "")
	  public List<JsonNode> getItems() {
	    return items;
	  }

	  public void setItems(List<JsonNode> items) {
	    this.items = items;
	  }

	  public PagedSmartApps links(Links links) {
	    this.links = links;
	    return this;
	  }

	   /**
	   * Get links
	   * @return links
	  **/
	  @ApiModelProperty(value = "")
	  public Links getLinks() {
	    return links;
	  }

	  public void setLinks(Links links) {
	    this.links = links;
	  }


	  @Override
	  public boolean equals(java.lang.Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    PagedSmartApps pagedDevices = (PagedSmartApps) o;
	    return Objects.equals(this.items, pagedDevices.items) &&
	        Objects.equals(this.links, pagedDevices.links);
	  }

	  @Override
	  public int hashCode() {
	    return Objects.hash(items, links);
	  }


	  @Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class PagedDevices {\n");
	    
	    sb.append("    items: ").append(toIndentedString(items)).append("\n");
	    sb.append("    links: ").append(toIndentedString(links)).append("\n");
	    sb.append("}");
	    return sb.toString();
	  }

	  /**
	   * Convert the given object to string with each line indented by 4 spaces
	   * (except the first line).
	   */
	  private String toIndentedString(java.lang.Object o) {
	    if (o == null) {
	      return "null";
	    }
	    return o.toString().replace("\n", "\n    ");
	  }
	  
}
