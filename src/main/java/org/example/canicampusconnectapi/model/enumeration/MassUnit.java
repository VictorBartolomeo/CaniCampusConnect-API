package org.example.canicampusconnectapi.model.enumeration;

import com.fasterxml.jackson.annotation.JsonView;
import org.example.canicampusconnectapi.view.owner.OwnerViewDog;

@JsonView(OwnerViewDog.class)
public enum MassUnit {
    KILOGRAM,
    POUND,
}
