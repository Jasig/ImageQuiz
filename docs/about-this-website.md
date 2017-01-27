# About this website

This ImageQuiz documentation website is generated from the files in the `docs` directory of the repository, using the open source static site generator tool `Jekyll` as provided by GitHub Pages.

## The latest `master` branch `docs` folder is controlling

GitHub Pages will (try to) build and deploy the very latest version of the website source from the `docs` folder in the `master` branch of the repository. To update the contents of that folder in the tip of `master` is to update the website.

## Editing

The documentation source files are text files in that `docs` directory, in a simple markup language called Markdown. Mostly, you can just edit them, and Jekyll and GitHub Pages will do the right thing. 

If you propose your changes as a Pull Request, GitHub Pages will go ahead and try to build the changed website and note on that Pull Request about its success or failure, so you can get a heads up of issues even before someone merges them to `master`.

## Building the documentation locally

You can build the documentation locally, e.g. to support working on it, using the `Jekyll` tool, which has its own documentation. 

```shell
bundle exec jekyll serve
```

Building locally would let you preview what your changes will look like before relying upon GitHub Pages to build them. 

The Jekyll tool happens to be implemented in Ruby; the included `GEMFILE` declares the Ruby dependencies ("gems") you'd need locally and the included `_config.yml` configures `Jekyll` with the same constraints that GitHub Pages provides in its usage of Jekyll, so that you're less likely to accidentally try to do something in your local Jekyll work that GitHub Pages would disallow, and so that the same shortcuts and assumptions GitHub pages make will be reflected locally.

## Getting fancy

When it comes right down to it you can embed plain old HTML in Markdown files, so you can do anything.

But don't do that. Use the features of Markdown and implement a custom Jekyll theme if you want to get fancy.

Short of using a custom theme, you could use a theme included with GitHub Pages. Currently this uses the theme `jekyll-theme-dinky`; there are several others to choose from.
