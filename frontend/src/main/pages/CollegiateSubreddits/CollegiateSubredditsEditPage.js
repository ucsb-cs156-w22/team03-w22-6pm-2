import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import { useParams } from "react-router-dom";
import CollegiateSubredditsForm from "main/components/CollegiateSubreddits/CollegiateSubredditsForm";
import { Navigate } from 'react-router-dom'
import { useBackend, useBackendMutation } from "main/utils/useBackend";
import { toast } from "react-toastify";

export default function CollegiateSubredditsEditPage() {
  let { id } = useParams();

  const { data: CollegiateSubreddit, error: error, status: status } =
    useBackend(
      // Stryker disable next-line all : don't test internal caching of React Query
      [`/api/CollegiateSubreddit?id=${id}`],
      {  // Stryker disable next-line all : GET is the default, so changing this to "" doesn't introduce a bug
        method: "GET",
        url: `/api/CollegiateSubreddits`,
        params: {
          id
        }
      }
    );


  const objectToAxiosPutParams = (CollegiateSubreddit) => ({
    url: "/api/CollegiateSubreddits",
    method: "PUT",
    params: {
      id: CollegiateSubreddit.id,
    },
    data: {
      location: CollegiateSubreddit.location,
      name: CollegiateSubreddit.name,
      subreddit: CollegiateSubreddit.subreddit
    }
  });

  const onSuccess = (CollegiateSubreddit) => {
    toast(`CollegiateSubreddit Updated - id: ${CollegiateSubreddit.id} name: ${CollegiateSubreddit.name}`);
  }

  const mutation = useBackendMutation(
    objectToAxiosPutParams,
    { onSuccess },
    // Stryker disable next-line all : hard to set up test for caching
    [`/api/CollegiateSubreddits?id=${id}`]
  );

  const { isSuccess } = mutation

  const onSubmit = async (data) => {
    mutation.mutate(data);
  }

  if (isSuccess) {
    return <Navigate to="/collegiatesubreddits/list" />
  }

  return (
    <BasicLayout>
      <div className="pt-2">
        <h1>Edit CollegiateSubreddit</h1>
        {CollegiateSubreddit &&
          <CollegiateSubredditsForm initialCollegiateSubreddit={CollegiateSubreddit} submitAction={onSubmit} buttonLabel="Update" />
        }
      </div>
    </BasicLayout>
  )
}