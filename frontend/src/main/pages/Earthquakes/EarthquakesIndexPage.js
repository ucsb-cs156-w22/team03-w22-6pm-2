
import React from 'react'
import { useBackend, useBackendMutation } from 'main/utils/useBackend';

import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import EarthquakesTable from 'main/components/Earthquakes/EarthquakesTable';
import { useCurrentUser } from 'main/utils/currentUser'
import { Button } from 'react-bootstrap';
import { toast } from "react-toastify";


export default function EarthquakesIndexPage() {

  const currentUser = useCurrentUser();

  const { data: earthquakes, error: _error, status: _status } =
    useBackend(
      // Stryker disable next-line all : don't test internal caching of React Query
      ["/api/earthquakes/all"],
      { method: "GET", url: "/api/earthquakes/all" },
      []
    );

    function Purge(){
      let purge = useBackendMutation(
          () => ({ url: "/api/earthquakes/purge", method: "purge" }),
          { onSuccess: () => { toast("Successfully purged earthquakes!"); } },
          // Stryker disable next-line all : don't test internal caching of React Query
          ["/api/earthquakes/all"],
        );
        return (
          <Button onClick={ () => { purge.mutate(); } } data-testid="purge">
            Purge
          </Button>
        );
      }
    }
  return (
    <BasicLayout>
      <div className="pt-2">
        <h1>Students</h1>
        <EarthquakesTable earthquakes={earthquakes} currentUser={currentUser} />
        <Purge/>
      </div>
    </BasicLayout>
  )
}
/*
import BasicLayout from "main/layouts/BasicLayout/BasicLayout";

export default function EarthquakesIndexPage() {
  return (
    <BasicLayout>
      <div className="pt-2">
        <h1>Earthquakes</h1>
        <p>
          This is where the index page will go
        </p>
      </div>
    </BasicLayout>
  )
}*/