#!/usr/bin/env bash

# This script helps to delete batches of workflow runs given their name.
# Batches sizes are not constant because fetching of wf runs is made with pages of 25 runs
# that contains all the wf runs from Konstellation whatever their name are.

workflow_name=$1
json_columns="[.id, .display_title, .conclusion, .name]"
response=$(gh api -X GET /repos/gabrieldrn/Konstellation/actions/runs -f page=1 -f per_page=25)
batch=$(echo "$response" | jq -r ".workflow_runs[] | $json_columns | @tsv" | grep $workflow_name)
count=$(echo "$batch" | wc -l | xargs)
if [[ -z $batch  ]]; then
	echo "No workflow runs found with name \"$workflow_name\""; exit 1
fi

echo "Here is a batch of $count workflow runs named \"$workflow_name\" to be deleted:"
echo "$batch"
echo "Proceed to delete? [Y/n]"
read -r delete
if [[ $delete == "Y" ]]; then
	ids=$(echo "$batch" | awk '{print $1}')
	for id in $ids; do
		echo "Deleting $id..."
		gh api --silent -X DELETE "/repos/gabrieldrn/Konstellation/actions/runs/$id"
		sleep 0.5
	done
	echo "Done."
else
	echo "Canceled."
fi
