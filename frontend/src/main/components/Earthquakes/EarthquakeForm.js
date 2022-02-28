import React, {useState} from 'react'
import { Button, Form } from 'react-bootstrap';
import { useForm } from 'react-hook-form'
import { useNavigate } from 'react-router-dom'


function EarthquakeForm({ initialEarthquakes, retrieveAction, buttonLabel="Create" }) {

    // Stryker disable all
    const {
        register,
        formState: { errors },
        handleSubmit,
    } = useForm(
        { defaultValues: initialEarthquakes || {}, }
    );
    // Stryker enable all

    const navigate = useNavigate();

    // For explanation, see: https://stackoverflow.com/questions/3143070/javascript-regex-iso-datetime
    // Note that even this complex regex may still need some tweaks

    // Stryker disable next-line Regex
    /*const isodate_regex = /(\d{4}-[01]\d-[0-3]\dT[0-2]\d:[0-5]\d:[0-5]\d\.\d+)|(\d{4}-[01]\d-[0-3]\dT[0-2]\d:[0-5]\d:[0-5]\d)|(\d{4}-[01]\d-[0-3]\dT[0-2]\d:[0-5]\d)/i;

    // Stryker disable next-line all
    const yyyyq_regex = /((19)|(20))\d{2}[1-4]/i; // Accepts from 1900-2099 followed by 1-4.  Close enough.
*/
    return (

        <Form onSubmit={handleSubmit(submitAction)}>
            <Form.Group className="mb-3" >
                <Form.Label htmlFor="dist">distance in km from Storke Tower</Form.Label>
                <Form.Control
                    data-testid="EarthquakeForm-dist"
                    id="dist"
                    type="text"
                    isInvalid={Boolean(errors.dist)}
                    {...register("dist", { required: 'enter a valid distance' })}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.dist && 'enter a valid distance in km'}
                </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="minMag">minimum magnitude of earthquake</Form.Label>
                <Form.Control
                    data-testid="EarthquakeForm-minMag"
                    id="minMag"
                    type="text"
                    isInvalid={Boolean(errors.name)}
                    {...register("minMag", {
                        required: "enter a valid magnitude 1-10."
                    })}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.name?.message}
                </Form.Control.Feedback>
            </Form.Group>


            <Button
                type="retrieve"
                data-testid="EarthquakeForm-retrieve"
            >
                {buttonLabel}
            </Button>
            <Button
                variant="Secondary"
                onClick={() => navigate(-1)}
                data-testid="EarthquakeForm-cancel"
            >
                Cancel
            </Button>

        </Form>

    )
}

export default EarthquakeForm;
