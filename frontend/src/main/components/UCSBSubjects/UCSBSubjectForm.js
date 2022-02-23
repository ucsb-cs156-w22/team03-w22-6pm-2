import React, {useState} from 'react'
import { Button, Form } from 'react-bootstrap';
import { useForm } from 'react-hook-form'
import { useNavigate } from 'react-router-dom'


function UCSBSubjectForm({ initialUCSBSubject, submitAction, buttonLabel="Create" }) {

    // Stryker disable all
    const {
        register,
        formState: { errors },
        handleSubmit,
    } = useForm(
        { defaultValues: initialUCSBSubject || {}, }
    );
    // Stryker enable all

    const navigate = useNavigate();



    return (

        <Form onSubmit={handleSubmit(submitAction)}>

            {initialUCSBSubject && (
                <Form.Group className="mb-3" >
                    <Form.Label htmlFor="id">Id</Form.Label>
                    <Form.Control
                        data-testid="UCSBSubjectForm-id"
                        id="id"
                        type="text"
                        {...register("id")}
                        value={initialUCSBSubject.id}
                        disabled
                    />
                </Form.Group>
            )}

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="subjectCode">Subject Code</Form.Label>
                <Form.Control
                    data-testid="UCSBSubjectForm-subjectCode"
                    id="subjectCode"
                    type="text"
                    isInvalid={Boolean(errors.subjectCode)}
                    {...register("subjectCode", { required: "SubjectCode is required" })}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.subjectCode?.message}
                </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="subjectTranslation">SubjectTranslation</Form.Label>
                <Form.Control
                    data-testid="UCSBSubjectForm-subjecdtTranslation"
                    id="subjectTranslation"
                    type="text"
                    isInvalid={Boolean(errors.subjectTranslation)}
                    {...register("subjectTranslation", {
                        required: "SubjectTranslation is required."
                    })}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.subjectTranslation?.message}
                </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="deptCode">DeptCode</Form.Label>
                <Form.Control
                    data-testid="UCSBSubjectForm-deptCode"
                    id="deptCode"
                    type="text"
                    isInvalid={Boolean(errors.deptCode)}
                    {...register("deptCode", { required: "deptCode is required." })}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.deptCode?.message}
                </Form.Control.Feedback>
            </Form.Group>


            <Form.Group className="mb-3" >
                <Form.Label htmlFor="collegeCode">CollegeCode</Form.Label>
                <Form.Control
                    data-testid="UCSBSubjectForm-CollegeCode"
                    id="collegeCode"
                    type="text"
                    isInvalid={Boolean(errors.collegeCode)}
                    {...register("collegeCode", { required: "collegeCode is required." })}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.collegeCode?.message}
                </Form.Control.Feedback>
            </Form.Group>


            <Form.Group className="mb-3" >
                <Form.Label htmlFor="relatedDeptCode">RelatedDeptCode</Form.Label>
                <Form.Control
                    data-testid="UCSBSubjectForm-relatedDeptCode"
                    id="relatedDeptCode"
                    type="text"
                    isInvalid={Boolean(errors.relatedDeptCode)}
                    {...register("relatedDeptCode", { required: "relatedDeptCode is required." })}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.relatedDeptCode?.message}
                </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="inactive">Inactive</Form.Label>
                <Form.Control
                    data-testid="UCSBSubjectForm-inactive"
                    id="inactive"
                    type="text"
                    isInvalid={Boolean(errors.inative)}
                    {...register("inactive", { required: "inactive is required." })}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.inactive?.message}
                </Form.Control.Feedback>
            </Form.Group>

            <Button
                type="submit"
                data-testid="UCSBSubjectForm-submit"
            >
                {buttonLabel}
            </Button>
            <Button
                variant="Secondary"
                onClick={() => navigate(-1)}
                data-testid="UCSBSubjectForm-cancel"
            >
                Cancel
            </Button>

        </Form>

    )
}

export default UCSBSubjectForm;
