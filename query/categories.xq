let $employees := /company/division/employees/employee 
let $category := /company/category

let $workers := for $e in $employees[@category = "worker"] return $e
let $workersResult := for $w in $workers return <employee salary="{$w/salary}">{data($w/name)}</employee>

let $developers := for $e in $employees[@category = "developer"] return $e
let $developersResult := for $d in $developers return <employee salary="{$d/salary}">{data($d/name)}</employee>

let $contractors := for $e in $employees[@category = "contractors"] return $e
let $contractorsResult := for $c in $contractors return <employee salary="{$c/salary}">{data($c/name)}</employee>

let $salaryWorkers := sum($employees[@category="worker"]/salary)
let $salaryDevelopers := sum($employees[@category="developer"]/salary)
let $feeContractors := sum($employees[@category="contractor"]/fee)

let $workersReturn := 
<category id="worker" total_salary_or_fee="{$salaryWorkers}">
  <name>{data(/company/category[@cid="worker"]/name)}</name>
  {$workersResult}
</category>

let $developersReturn := 
<category id="developer" total_salary_or_fee="{$salaryDevelopers}">
  <name>{data(/company/category[@cid="developer"]/name)}</name>
  {$developersResult}
</category>

let $contractorsReturn := 
<category id="contractor" total_salary_or_fee="{$feeContractors}">
  <name>{data(/company/category[@cid="contractor"]/name)}</name>
  {$contractorsResult}
</category>

return <categories>{$workersReturn, $developersReturn, $contractorsReturn}</categories>  





   