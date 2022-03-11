import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import PersonalScheduleForm from "main/components/PersonalSchedules/PersonalScheduleForm";
import { Navigate } from 'react-router-dom'
import { useBackendMutation } from "main/utils/useBackend";
import { toast } from "react-toastify";

export default function PersonalScheduleCreatePage() {

  const objectToAxiosParams = (personalSchedule) => ({
    url: "/api/personalschedules/post",
    method: "POST",
    params: {
      name: personalSchedule.name,
      description: personalSchedule.description,
      quarter: personalSchedule.quarter
    }
  });

  const onSuccess = (personalSchedule) => {
    toast(`New personalSchedule Created - id: ${personalSchedule.id} name: ${personalSchedule.name}`);
  }

  const mutation = useBackendMutation(
    objectToAxiosParams,
     { onSuccess }, 
     // Stryker disable next-line all : hard to set up test for caching
     ["/api/personalschedules/all"]
     );

  const { isSuccess } = mutation

  const onSubmit = async (data) => {
    mutation.mutate(data);
  }

  if (isSuccess) {
    return <Navigate to="/personalschedules/list" />
  }

  return (
    <BasicLayout>
      <div className="pt-2">
        <h1>Create New Personal Schedule</h1>

        <PersonalScheduleForm submitAction={onSubmit} />

      </div>
    </BasicLayout>
  )
}