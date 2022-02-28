import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import CollegiateSubredditsForm from "main/components/CollegiateSubreddits/CollegiateSubredditsForm";
import { Navigate } from 'react-router-dom';
import { useBackendMutation } from "main/utils/useBackend";
import { toast } from 'react-toastify';
import { Col } from "react-bootstrap";


export default function CollegiateSubredditsCreatePage() {

  const objectToAxiosParams = (CollegiateSubreddit) => ({
    url: "/api/CollegiateSubreddits/post",
    method: "POST",
    params: {
      name: CollegiateSubreddit.name,
      location: CollegiateSubreddit.location,
      subreddit: CollegiateSubreddit.subreddit
    }
  });

  const onSuccess = (CollegiateSubreddit) => {
    toast(`New CollegiateSubreddit Created - id: ${CollegiateSubreddit.id} name: ${CollegiateSubreddit.name}`);
  }

  const mutation = useBackendMutation(
    objectToAxiosParams,
    { onSuccess },
    // Stryker disable next-line all : hard to set up test for caching
    ["/api/CollegiateSubreddits/all"]
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
        <h1>Create New CollegiateSubreddit</h1>

        <CollegiateSubredditsForm submitAction={onSubmit} />
      </div>
    </BasicLayout>
  )
}