import React from "react";
import ScrollableAnchor from "react-scrollable-anchor";

const MapSection = () => (
  <ScrollableAnchor id="contact">
    <section className="map">
      <iframe
      title="where-we-are-located"
        width="100%"
        height="100%"
        frameBorder="0"
        scrolling="no"
        marginHeight="0"
        marginWidth="0"
        src="https://maps.google.com/maps?q=47.473366,19.059888&hl=es;z=14&amp;output=embed"
      />
      <br />
    </section>
  </ScrollableAnchor>
);
export default MapSection;
