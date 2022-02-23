import React from 'react';

import UCSBDatesTable from "main/components/UCSBSubjects/UCSBSubjectsTable";
import { ucsbDatesFixtures } from 'fixtures/ucsbDatesFixtures';

export default {
    title: 'components/UCSBSubjects/UCSBSubjectsTable',
    component: UCSBSubjectsTable
};

const Template = (args) => {
    return (
        <UCSBSubjectsTable {...args} />
    )
};

export const Empty = Template.bind({});

Empty.args = {
    subjects: []
};

export const ThreeSubjects = Template.bind({});

ThreeSubjects.args = {
    subjects: ucsbSubjectsFixtures.threeSubjecdts
};


